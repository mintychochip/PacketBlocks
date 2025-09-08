plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
    `java-library`
}

group = "org.example"
version = "1.0-SNAPSHOT"

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
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.2")
    implementation("com.google.inject:guice:7.0.0")
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