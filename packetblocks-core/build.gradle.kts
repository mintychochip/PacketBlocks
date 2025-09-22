plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
    `java-library`
}

group = "org.aincraft"
version = "1.0.2"

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation("org.mongodb:bson:4.11.1")
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.2")
    implementation("com.google.inject:guice:7.0.0")

    // ✅ Use local pb-api project instead of Paper API
    implementation(project(":packetblocks-api")) {
        // Exclude Paper API module if pb-api pulls it transitively
        exclude(group = "io.papermc.paper", module = "paper-api")
    }

    // Keep dev bundle for compilation & remapping, but it won’t end up in the plugin JAR
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}

tasks.test {
    useJUnitPlatform()
}
