import edu.wpi.first.toolchain.NativePlatforms

plugins {
    id("java")
    id("edu.wpi.first.GradleRIO") version "2026.2.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "io.github.arrowvark.jfoxlog"
version = "0.0.1"

val ROBOT_MAIN_CLASS = "io.github.arrowvark.jfoxlog.sample.Main"

repositories {
    mavenCentral()
    maven { url = uri("https://frcmaven.wpi.edu/artifactory/release") }
    maven { url = uri("https://frcmaven.wpi.edu/artifactory/littletonrobotics-mvn-release/") }
}

wpi.java.debugJni = false

dependencies {
    implementation(project(":library"))

    wpi.java.deps.wpilibAnnotations().forEach { add("annotationProcessor", it) }
    wpi.java.deps.wpilib().forEach             { add("implementation",      it) }
    wpi.java.vendor.java().forEach             { add("implementation",      it) }

    wpi.java.deps.wpilibJniDebug(NativePlatforms.desktop).forEach  { add("nativeDebug",      it) }
    wpi.java.vendor.jniDebug(NativePlatforms.desktop).forEach      { add("nativeDebug",      it) }
    wpi.sim.enableDebug().forEach                                   { add("simulationDebug",  it) }

    wpi.java.deps.wpilibJniRelease(NativePlatforms.desktop).forEach { add("nativeRelease",    it) }
    wpi.java.vendor.jniRelease(NativePlatforms.desktop).forEach     { add("nativeRelease",    it) }
    wpi.sim.enableRelease().forEach                                  { add("simulationRelease",it) }

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    val akitJson = groovy.json.JsonSlurper().parseText(
        File("${projectDir.absolutePath}/vendordeps/AdvantageKit.json").readText()
    ) as Map<*, *>
    annotationProcessor("org.littletonrobotics.akit:akit-autolog:${akitJson["version"]}")
}

tasks.test {
    useJUnitPlatform()
}

wpi.sim.addGui().defaultEnabled = true
wpi.sim.addDriverstation()

tasks.jar {
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    manifest(edu.wpi.first.gradlerio.GradleRIOPlugin.javaManifest(ROBOT_MAIN_CLASS))
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

wpi.java.configureTestTasks(tasks.test.get())

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-XDstringConcat=inline")
}