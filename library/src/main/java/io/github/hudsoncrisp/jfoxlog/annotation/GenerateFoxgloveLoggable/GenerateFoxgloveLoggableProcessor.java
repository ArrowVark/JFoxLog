package io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.github.victools.jsonschema.generator.*;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.palantir.javapoet.*;
import io.github.hudsoncrisp.jfoxlog.annotation.FieldTransformation.FieldTransformation;
import io.github.hudsoncrisp.jfoxlog.annotation.LoggingParameters;
import io.github.hudsoncrisp.jfoxlog.annotation.Util;
import io.github.hudsoncrisp.jfoxlog.foxglove.*;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveLoggingFrequencyInfo;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes("io.github.hudsoncrisp.jfoxlog.annotation.GenerateFoxgloveLoggable.GenerateFoxgloveLoggable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class GenerateFoxgloveLoggableProcessor extends AbstractProcessor {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private SchemaGenerator schemaGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);

        // Build the generator once — reused for every annotated class
        SchemaGeneratorConfigBuilder configBuilder = new SchemaGeneratorConfigBuilder(
                SchemaVersion.DRAFT_2020_12,
                OptionPreset.PLAIN_JSON          // or JAVA_OBJECT for more detail
        )
                .with(new JacksonModule());              // honours @JsonProperty, @JsonIgnore, etc.

        // Common useful options
        configBuilder.forFields()
                .withRequiredCheck(field -> field.getAnnotationConsideringFieldAndGetterIfSupported(
                        JsonProperty.class) != null
                        && field.getAnnotationConsideringFieldAndGetterIfSupported(
                        JsonProperty.class).required())
                .withDescriptionResolver(field -> {
                    var ann = field.getAnnotationConsideringFieldAndGetter(
                            JsonPropertyDescription.class);
                    return ann != null ? ann.value() : null;
                });

        schemaGenerator = new SchemaGenerator(configBuilder.build());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(GenerateFoxgloveLoggable.class)) {
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.NOTE,
                    "Found annotation for: " + element.getSimpleName()
            );

            if (element.getKind() != ElementKind.CLASS) continue;

            TypeElement classElement = (TypeElement) element;
            GenerateFoxgloveLoggable annotation = classElement.getAnnotation(GenerateFoxgloveLoggable.class);
            String className = classElement.getQualifiedName().toString();
            String simpleName = classElement.getSimpleName().toString();

//            Class<?> clazz;
//            try {
//                 clazz = AnnotationUtils.buildFromFields(
//                         classElement,
//                         getClass().getClassLoader(),
//                         processingEnv.getElementUtils()
//                 );
//            } catch (Exception e) {
//                processingEnv.getMessager().printMessage(
//                        Diagnostic.Kind.ERROR,
//                        "Failed to build schema proxy for " + classElement.getSimpleName() + ": "
//                                + e.getClass().getSimpleName() + ": " + e.getMessage(),
//                        classElement
//                );
//                continue;
////                throw new RuntimeException(e);
//            }
//
//            String schema = schemaGenerator.generateSchema(clazz).toPrettyString();
            String schema = Util.buildSchemaFromLoggedFields(classElement, simpleName);

            if (annotation == null) continue;

            String packageName = processingEnv.getElementUtils().getPackageOf(classElement).toString();

            boolean autoLogged = Util.hasAnnotation(element, "org.littletonrobotics.junction.AutoLog");

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Has AutoLog? " + autoLogged);

            FieldSpec gson = FieldSpec
                    .builder(Gson.class, "GSON", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
                    .initializer("new $T()", Gson.class)
                    .build();

            FieldSpec loggedFields = FieldSpec
                    .builder(Object.class, "LOGGED_FIELDS", Modifier.PRIVATE, Modifier.FINAL)
                    .initializer("new LoggedFields()")
                    .build();

//            FieldSpec channel = FieldSpec
//                    .builder(FoxgloveChannel.class, "CHANNEL", Modifier.PRIVATE, Modifier.FINAL)
//                    .initializer(
//                            "$T.requestNewChannel(\"" + annotation.parentPath() + "/" + annotation.name() + "\", " +
//                                    "$T." + annotation.loggingType() + ", this)",
//                            FoxgloveWebSocketServer.class,
//                            annotation.loggingType().getClass()
//                    ).build();

            MethodSpec getSchema = MethodSpec
                    .methodBuilder("getSchema")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", schema)
                    .build();

            MethodSpec getSchemaName = MethodSpec
                    .methodBuilder("getSchemaName")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", "generated." + simpleName)
                    .build();

            MethodSpec toJson = MethodSpec
                        .methodBuilder("toJson")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(String.class)
                        .addStatement("return GSON.toJson(((LoggedFields) LOGGED_FIELDS).get())")
                        .build();

            String autoLoggedName = classElement.getSimpleName() + "AutoLogged";

            class ClassConstructor {
                TypeSpec generateClass(String name, boolean autoLogClass) {
                    TypeSpec.Builder generatedClass = TypeSpec
                            .classBuilder(name)
                            .addJavadoc("Generated class that allows for the automatic logging of a class to Foxglove")
                            .addModifiers(Modifier.PUBLIC)
                            .addSuperinterface(FoxgloveLoggable.class)
                            .addMethod(getSchema)
                            .addMethod(getSchemaName)
                            .addMethod(toJson)
                            .addField(gson)
                            .addField(loggedFields);
//                            .addField(channel);

                    MethodSpec.Builder setupChildObjectsBuilder = MethodSpec
                            .methodBuilder("setupChildObjects")
                            .addParameter(String.class, "parentTopic")
                            .addAnnotation(Override.class)
                            .addModifiers(Modifier.PUBLIC);

                    generatedClass.addType(generateLoggedFieldsClass(autoLogClass, generatedClass, setupChildObjectsBuilder));

                    if (autoLogged && autoLogClass) {
                        generatedClass.superclass(ClassName.get(packageName, autoLoggedName));
                    } else {
                        generatedClass.superclass(element.asType());
                    }

//                    if (annotation.createLogObject()) {
//                        FieldSpec logObject = FieldSpec
//                                .builder(Log.class, "LOG_OBJECT", Modifier.PRIVATE, Modifier.FINAL)
//                                .initializer(
//                                        "new $T(" +
//                                                "$T.INFO.getCode(), " +
//                                                "\"Created new Log object for " + simpleName +
//                                                "\")",
//                                        Log.class,
//                                        LogLevel.class
//                                ).build();
//
//                        FieldSpec logObjectChannel = FieldSpec
//                                .builder(FoxgloveChannel.class, "LOG_OBJECT_CHANNEL", Modifier.PRIVATE, Modifier.FINAL)
//                                .initializer(
//                                        "$T.requestNewChannel(\"" + annotation.parentPath() + "/" + annotation.name() + "/Log\", LOG_OBJECT)",
//                                        FoxgloveWebSocketServer.class
//                                ).build();
//
//                        MethodSpec logMessage = MethodSpec
//                                .methodBuilder("logMessage")
//                                .returns(void.class)
//                                .addParameter(int.class, "level") // Temp int, change to log level later
//                                .addParameter(String.class, "message")
//                                .addModifiers(Modifier.PUBLIC)
//                                .addStatement("LOG_OBJECT.message(level, message)")
////                                .addStatement("LOG_OBJECT_CHANNEL.setLoggable(LOG_OBJECT)")
//                                .build();
//
//                        generatedClass.addField(logObject);
//                        generatedClass.addField(logObjectChannel);
//                        generatedClass.addMethod(logMessage);
//                    }

                    return generatedClass.build();
                }

                TypeSpec generateLoggedFieldsClass(
                        boolean autoLogClass,
                        TypeSpec.Builder generatedClassBuilder,
                        MethodSpec.Builder setupChildObjectsBuilder
                ) {
                    TypeSpec.Builder loggedFieldsClass = TypeSpec
                            .classBuilder("LoggedFields")
                            .addModifiers(Modifier.PRIVATE);

                    MethodSpec.Builder get = MethodSpec
                            .methodBuilder("get")
                            .returns(Object.class);

                    for (Element field : element.getEnclosedElements()) {
                        if (field.getKind() != ElementKind.FIELD) continue;

                        String simpleName = field.getSimpleName().toString();
                        String outerRef = element.getSimpleName()
                                        + "FoxgloveLoggable"
                                        + (autoLogClass ? "AutoLogged" : "")
                                        + ".this.";

                        FieldSpec fieldSpec = FieldSpec
                                .builder(TypeName.get(field.asType()), simpleName)
                                .initializer(outerRef + simpleName)
                                .build();

                        String getStatement = simpleName + " = " + outerRef + simpleName;

//                        VariableElement nonDeclaredTypeElement = (VariableElement) field;
                        var transformAnnotation = field.getAnnotation(FieldTransformation.class);
                        var loggingParameters = field.getAnnotation(LoggingParameters.class);

                        if (transformAnnotation != null) {
                            String transformation = transformAnnotation.transform().replace("FIELD", outerRef + simpleName);

                            TypeName returnedTypeName;
                            try {
                                returnedTypeName = TypeName.get(transformAnnotation.returnedType());
                            } catch (MirroredTypeException e) {
                                returnedTypeName = TypeName.get(e.getTypeMirror());
                            }

                            fieldSpec = FieldSpec
                                    .builder(returnedTypeName, simpleName)
                                    .initializer(transformation)
                                    .build();
                            getStatement = simpleName + " = " + transformation;

                            loggedFieldsClass.addField(fieldSpec);
                            get.addStatement(getStatement);
                            continue;
                        }

                        if (field.asType().getKind() == TypeKind.DECLARED) {
                            TypeElement typeElement = (TypeElement) ((DeclaredType) field.asType()).asElement();
                            String qualifiedName = typeElement.getQualifiedName().toString();
                            double millisecondLead = 0;
                            FoxgloveLoggingFrequencyInfo.DataRetrieveType dataRetrieveType = FoxgloveLoggingFrequencyInfo.DataRetrieveType.NEW_DATA;

                            if (loggingParameters != null) {
                                millisecondLead = loggingParameters.millisecondLead();
                                if (loggingParameters.dataRetrieveType() != null) {
                                    dataRetrieveType = loggingParameters.dataRetrieveType();
                                }
                            }

                            Util.processComplexTypes(field, millisecondLead, dataRetrieveType, simpleName, generatedClassBuilder, get, setupChildObjectsBuilder);
                        }

                        loggedFieldsClass.addField(fieldSpec);
                        get.addStatement(getStatement);
                    }

                    get.addStatement("return this");
                    generatedClassBuilder.addMethod(setupChildObjectsBuilder.build());

                    return loggedFieldsClass
                            .addMethod(get.build())
                            .build();
                }
            }

            ClassConstructor constructor = new ClassConstructor();
            TypeSpec builtClass = constructor
                    .generateClass(element.getSimpleName() + "FoxgloveLoggable", false);

            TypeSpec autoLoggedClass = null;
            if (autoLogged) {
                 autoLoggedClass = constructor
                        .generateClass(element.getSimpleName() + "FoxgloveLoggableAutoLogged", true);
            }

            try {
                JavaFile.builder(packageName, builtClass)
                        .build()
                        .writeTo(processingEnv.getFiler());

                if (autoLogged) {
                    JavaFile.builder(packageName, autoLoggedClass)
                            .build()
                            .writeTo(processingEnv.getFiler());
                }

                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.NOTE,
                        "Generated FoxgloveLogged class for: " + element.getSimpleName()
                );
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "FoxgloveLogSubsystem processing failed for " + element + ": " + e.getMessage()
                );
            }
        }

        return true;
    }
}
