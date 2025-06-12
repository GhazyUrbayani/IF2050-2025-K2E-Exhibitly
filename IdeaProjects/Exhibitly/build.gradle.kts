plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    modularity.inferModulePath.set(true)
}

application {
    mainModule.set("org.example.exhibitly")
    mainClass.set("org.example.exhibitly.models.HelloApplication")
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.swing")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.mysql:mysql-connector-j:9.3.0")
    implementation("net.synedra:validatorfx:0.5.0") {
        exclude(group = "org.openjfx")
    }
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    
    testImplementation("org.testfx:testfx-core:4.0.17")
    testImplementation("org.testfx:testfx-junit5:4.0.17")
    
    testImplementation("org.hamcrest:hamcrest:2.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
    jvmArgs = listOf(
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.base/javafx.beans=ALL-UNNAMED",
        "--add-reads", "org.example.exhibitly=org.junit.jupiter.api,org.testfx.junit5,org.testfx.core",
        "--add-opens=org.example.exhibitly/org.example.exhibitly.controller=ALL-UNNAMED",
        "--add-opens=org.example.exhibitly/org.example.exhibitly.models=ALL-UNNAMED",
        "--add-exports=javafx.graphics/com.sun.javafx.application=ALL-UNNAMED"
    )
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
