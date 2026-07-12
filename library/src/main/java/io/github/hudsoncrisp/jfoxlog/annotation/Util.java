package io.github.hudsoncrisp.jfoxlog.annotation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.palantir.javapoet.*;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import io.github.hudsoncrisp.jfoxlog.annotation.FieldTransformation.FieldTransformation;
import io.github.hudsoncrisp.jfoxlog.annotation.transforms.FrameTransform;
import io.github.hudsoncrisp.jfoxlog.foxglove.FoxgloveLoggable;
import io.github.hudsoncrisp.jfoxlog.foxglove.servers.websocket.FoxgloveChannel;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.Set;

public class Util {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    // Known schema shapes for types that are interfaces or have inaccessible concrete impls
    private static final java.util.Map<String, com.google.gson.JsonObject> KNOWN_TYPES = new java.util.HashMap<>();

    static {
        // All WPILib Measure subtypes serialize with these three fields at runtime
        com.google.gson.JsonObject measureSchema = new com.google.gson.JsonObject();
        measureSchema.addProperty("type", "object");
        com.google.gson.JsonObject measureProps = new com.google.gson.JsonObject();
        com.google.gson.JsonObject magnitudeNode = new com.google.gson.JsonObject();
        magnitudeNode.addProperty("type", "number");
        com.google.gson.JsonObject baseUnitMagnitudeNode = new com.google.gson.JsonObject();
        baseUnitMagnitudeNode.addProperty("type", "number");
//        com.google.gson.JsonObject unitNode = new com.google.gson.JsonObject();
//        unitNode.addProperty("type", "string");
        measureProps.add("magnitude", magnitudeNode);
        measureProps.add("baseUnitMagnitude", baseUnitMagnitudeNode);
//        measureProps.add("unit", unitNode);
        measureSchema.add("properties", measureProps);

        // Register all measure types
        for (String name : new String[]{
                "Angle", "AngularVelocity", "AngularAcceleration", "Distance",
                "LinearVelocity", "LinearAcceleration", "Current", "Voltage",
                "Temperature", "Time", "Frequency", "Mass", "Force", "Energy",
                "Power", "Torque", "Resistance", "Dimensionless", "MomentOfInertia"
        }) {
            KNOWN_TYPES.put("edu.wpi.first.units.measure." + name, measureSchema);
        }
    }

    public static Class<?> buildFromFields(TypeElement classElement, ClassLoader classLoader, Elements elements) throws Exception {

        // --- Extract only fields ---
        List<VariableElement> fields = classElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .map(e -> (VariableElement) e)
                .toList();

        // --- Start builder ---
        DynamicType.Builder<?> builder = new ByteBuddy()
                .subclass(Object.class)
                .name(classElement.getQualifiedName().toString() + "$SchemaProxy");

        // --- Add fields ---
        for (VariableElement field : fields) {
            TypeDescription fieldType = resolveType(field.asType(), elements);
            Set<Modifier> mods = field.getModifiers();

            builder = builder.defineField(
                    field.getSimpleName().toString(),
                    fieldType,
                    mapVisibility(mods),
                    isFinal(mods) ? FieldManifestation.FINAL : FieldManifestation.PLAIN
            );
        }

        return builder.make()
                .load(classLoader)
                .getLoaded();
    }

    // --- TypeMirror → TypeDescription ---
    private static TypeDescription resolveType(TypeMirror mirror, Elements elements) {
        switch (mirror.getKind()) {
            case BOOLEAN: return TypeDescription.ForLoadedType.of(boolean.class);
            case BYTE:    return TypeDescription.ForLoadedType.of(byte.class);
            case SHORT:   return TypeDescription.ForLoadedType.of(short.class);
            case INT:     return TypeDescription.ForLoadedType.of(int.class);
            case LONG:    return TypeDescription.ForLoadedType.of(long.class);
            case FLOAT:   return TypeDescription.ForLoadedType.of(float.class);
            case DOUBLE:  return TypeDescription.ForLoadedType.of(double.class);
            case CHAR:    return TypeDescription.ForLoadedType.of(char.class);
            case VOID:    return TypeDescription.ForLoadedType.of(void.class);
            case ARRAY: {
                TypeDescription component = resolveType(((ArrayType) mirror).getComponentType(), elements);
                return TypeDescription.ArrayProjection.of(component);
            }
            case DECLARED: {
                TypeElement typeElement = (TypeElement) ((DeclaredType) mirror).asElement();
                String binaryName = elements.getBinaryName(typeElement).toString();
                List<? extends TypeMirror> interfaces = typeElement.getInterfaces();

                for (TypeMirror inter : interfaces) {
                    if (inter.getKind() == TypeKind.DECLARED) {
                        TypeElement interElement = (TypeElement) ((DeclaredType) inter).asElement();
                        if (interElement.getQualifiedName().toString().equals("edu.wpi.first.units.Measure")) {
                            return TypeDescription.ForLoadedType.of(double.class);
                        }
                    }
                }

                switch (binaryName) {
                    case "edu.wpi.first.math.geometry.Rotation2d":
                        return TypeDescription.ForLoadedType.of(double.class);
                }

                try {
                    return TypeDescription.ForLoadedType.of(Class.forName(binaryName));
                } catch (ClassNotFoundException e) {
                    // Fall back to Enum.class for enums, Object.class for everything else
                    if (typeElement.getKind() == ElementKind.ENUM) {
                        return TypeDescription.ForLoadedType.of(Enum.class);
                    }
                    return TypeDescription.ForLoadedType.of(Object.class);
                }
            }
            default:
                return TypeDescription.ForLoadedType.of(Object.class);
        }
    }

    // --- Modifier helpers ---
    private static Visibility mapVisibility(Set<Modifier> mods) {
        if (mods.contains(Modifier.PUBLIC))    return Visibility.PUBLIC;
        if (mods.contains(Modifier.PROTECTED)) return Visibility.PROTECTED;
        if (mods.contains(Modifier.PRIVATE))   return Visibility.PRIVATE;
        return Visibility.PACKAGE_PRIVATE;
    }

    private static boolean isFinal(Set<Modifier> mods) {
        return mods.contains(Modifier.FINAL);
    }

    public static boolean hasAnnotation(Element element, String annotationClassName) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(mirror -> mirror.getAnnotationType()
                        .toString()
                        .equals(annotationClassName));
    }

    public static String buildSchemaFromLoggedFields(TypeElement classElement, String simpleName) {
        com.google.gson.JsonObject schema = new com.google.gson.JsonObject();
        schema.addProperty("$schema", "https://json-schema.org/draft/2020-12/schema");
        schema.addProperty("type", "object");
        schema.addProperty("title", "generated." + simpleName);

        com.google.gson.JsonObject properties = new com.google.gson.JsonObject();

        for (Element field : classElement.getEnclosedElements()) {
            if (field.getKind() != ElementKind.FIELD) continue;

            String fieldName = field.getSimpleName().toString();
            FieldTransformation ftAnnotation = field.getAnnotation(FieldTransformation.class);

            TypeMirror effectiveType;
            if (ftAnnotation != null) {
                // Use the declared returnedType of the transformation
                try {
                    ftAnnotation.returnedType(); // always throws
                    effectiveType = field.asType(); // unreachable
                } catch (MirroredTypeException e) {
                    effectiveType = e.getTypeMirror();
                }
            } else {
                effectiveType = field.asType();
            }

            properties.add(fieldName, typeMirrorToSchema(effectiveType));
        }

        schema.add("properties", properties);
        return GSON.toJson(schema);
    }

    private static com.google.gson.JsonObject typeMirrorToSchema(TypeMirror type) {
        com.google.gson.JsonObject node = new com.google.gson.JsonObject();

        switch (type.getKind()) {
            case BOOLEAN:
                node.addProperty("type", "boolean");
                break;
            case BYTE: case SHORT: case INT: case LONG:
                node.addProperty("type", "integer");
                break;
            case FLOAT: case DOUBLE:
                node.addProperty("type", "number");
                break;
            case ARRAY: {
                node.addProperty("type", "array");
                TypeMirror component = ((ArrayType) type).getComponentType();
                node.add("items", typeMirrorToSchema(component));
                break;
            }
            case DECLARED: {
                TypeElement te = (TypeElement) ((DeclaredType) type).asElement();
                String qName = te.getQualifiedName().toString();

                // 1. Exact known types (e.g. WPILib measures)
                if (KNOWN_TYPES.containsKey(qName)) {
                    return KNOWN_TYPES.get(qName);
                }

                if (qName.equals("java.lang.String")) {
                    node.addProperty("type", "string");
                } else if (te.getKind() == ElementKind.ENUM) {
                    node.addProperty("type", "string");
                } else if (te.getKind() == ElementKind.INTERFACE) {
                    // 2. Unknown interface — check if it extends a known one
                    com.google.gson.JsonObject matched = resolveInterfaceSchema(te);
                    if (matched != null) return matched;
                    node.addProperty("type", "object"); // fallback
                } else {
                    // 3. Concrete class — walk superclass chain as before
                    node.addProperty("type", "object");
                    com.google.gson.JsonObject nested = new com.google.gson.JsonObject();
                    TypeElement current = te;
                    while (current != null) {
                        String currentName = current.getQualifiedName().toString();
                        if (currentName.equals("java.lang.Object")) break;
                        for (Element enclosed : current.getEnclosedElements()) {
                            if (enclosed.getKind() != ElementKind.FIELD) continue;
                            if (enclosed.getModifiers().contains(Modifier.STATIC)) continue;
                            String fieldName = enclosed.getSimpleName().toString();
                            if (!nested.has(fieldName)) {
                                nested.add(fieldName, typeMirrorToSchema(enclosed.asType()));
                            }
                        }
                        TypeMirror superMirror = current.getSuperclass();
                        if (superMirror.getKind() != TypeKind.DECLARED) break;
                        current = (TypeElement) ((DeclaredType) superMirror).asElement();
                    }
                    if (nested.size() > 0) node.add("properties", nested);
                }
                break;
            }
            default:
                node.addProperty("type", "string"); // safe fallback
        }
        return node;
    }

    private static com.google.gson.JsonObject resolveInterfaceSchema(TypeElement te) {
        // Check this interface directly
        if (KNOWN_TYPES.containsKey(te.getQualifiedName().toString())) {
            return KNOWN_TYPES.get(te.getQualifiedName().toString());
        }
        // Recurse into super-interfaces
        for (TypeMirror iface : te.getInterfaces()) {
            if (iface.getKind() == TypeKind.DECLARED) {
                TypeElement ifaceElement = (TypeElement) ((DeclaredType) iface).asElement();
                com.google.gson.JsonObject result = resolveInterfaceSchema(ifaceElement);
                if (result != null) return result;
            }
        }
        return null;
    }

    public static void processComplexTypes(Element field, FoxgloveChannel.LoggingType loggingType, String simpleName, TypeSpec.Builder generatedClass, MethodSpec.Builder getMethod, MethodSpec.Builder setupChildObjectsBuilder) {

        var frameTransformAnnotation = field.getAnnotation(FrameTransform.class);

        if (frameTransformAnnotation != null) {
            createComplexChannelAndSetup(
                    simpleName,
                    loggingType,
                    generatedClass,
                    getMethod,
                    setupChildObjectsBuilder,
                    io.github.hudsoncrisp.jfoxlog.foxglove.types.scene.frameTransform.FrameTransform.class,
                    "new $T(" +
                            timestampNowFullReference() + ", " + // Calling Timestamp.now() twice, may change
                            "\"" + frameTransformAnnotation.parentFrame() + "\", " +
                            "\"" + frameTransformAnnotation.frameName() + "\", " +
                            "io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.vector.Vector3.kZero(), " +
                            "io.github.hudsoncrisp.jfoxlog.foxglove.types.geometry.Quaternion.kIdentity()" +
                            ").mutate(" + simpleName + ")" // Other .now() call
            );
            return;
        }

        Class<? extends FoxgloveLoggable> foxgloveType = null;

        // TODO: Add later, Pose does not have .from so it was upset
//        switch (type.getQualifiedName().toString()) {
//            case "edu.wpi.first.math.geometry.Pose2d",
//                 "edu.wpi.first.math.geometry.Pose3d":
//                foxgloveType = Pose.class;
//        }

        if (foxgloveType != null) {
            createComplexChannelAndSetup(
                    simpleName, loggingType, generatedClass, getMethod, setupChildObjectsBuilder, foxgloveType, "$T.from(" + simpleName + ")");
        }
    }

    private static void createComplexChannelAndSetup(
            String simpleName,
            FoxgloveChannel.LoggingType loggingType,
            TypeSpec.Builder generatedClass,
            MethodSpec.Builder getMethod,
            MethodSpec.Builder setupChildObjectsBuilder,
            Class<? extends FoxgloveLoggable> type,
            String $TLoggableString
    ) {
//        String channelName = simpleName + "Channel";
//        ParameterizedTypeName channelType = ParameterizedTypeName.get(
//                ClassName.get(FoxgloveChannel.class),
//                ClassName.get(type)
//        );

//        FieldSpec field = FieldSpec
//                .builder(channelType, channelName, Modifier.PRIVATE)
//                .initializer(
//                        "$T.<$T>requestNewChannel(\"" + path + "/" + simpleName + "\", $T.SERVER_DRIVEN, " + $TLoggableString + ")",
//                        FoxgloveWebSocketServer.class,
//                        type,
//                        FoxgloveChannel.LoggingType.class,
//                        type
//                ).build();

        String loggableObjectName = simpleName + type.getSimpleName();

        FieldSpec field = FieldSpec
                .builder(type, loggableObjectName, Modifier.PRIVATE)
                .initializer($TLoggableString, type)
                .build();

        generatedClass.addField(field);

        getMethod.addStatement(
                loggableObjectName + ".mutate(" + simpleName + ")"
        );

        setupChildObjectsBuilder
                .addStatement(
                        loggableObjectName + ".setupLogging(parentTopic + \"/" + simpleName + "\"," +
                                "$T.$L)", FoxgloveChannel.LoggingType.class, loggingType.name()
                );

    }

    public static String timestampNowFullReference() {
        return "io.github.hudsoncrisp.jfoxlog.foxglove.types.time.Timestamp.now()";
    }
}
