plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.18"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    java
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.ben-manes.caffeine:caffeine:3.2.2")
    implementation("com.google.inject:guice:7.0.0")
    implementation(project(":packetblocks-api")) {
        exclude(group = "io.papermc.paper", module = "paper-api")
    }
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
}

tasks.assemble {
    dependsOn(tasks.reobfJar)
}
