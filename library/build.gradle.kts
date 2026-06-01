plugins {
    id("java-library")
    id("maven-publish")
    id("edu.wpi.first.GradleRIO") version "2026.2.1"
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

group = "io.github.arrowvark"
version = "1.0-SNAPSHOT"

tasks.named("simulateJava") { enabled = false }
tasks.named("simulateJavaRelease") { enabled = false }

repositories {
    mavenCentral()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "JFoxLog"
        }
    }
}

dependencies {
    wpi.java.deps.wpilib().forEach { compileOnly(it) }
    wpi.java.vendor.java().forEach { compileOnly(it) }
    wpi.java.deps.wpilibAnnotations().forEach { annotationProcessor(it) }

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("com.google.code.gson:gson:2.13.2")
    implementation("org.java-websocket:Java-WebSocket:1.5.4")
    implementation("com.palantir.javapoet:javapoet:0.14.0")

    compileOnly("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    implementation("com.github.victools:jsonschema-generator:4.36.0")
    implementation("com.github.victools:jsonschema-module-jackson:4.36.0")
    implementation("com.github.victools:jsonschema-module-jakarta-validation:4.36.0")

    implementation("net.bytebuddy:byte-buddy:1.18.8")
}

tasks.test {
    useJUnitPlatform()
}