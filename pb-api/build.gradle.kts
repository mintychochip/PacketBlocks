plugins {
    `java-library`
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("com.gradleup.nmcp") version "1.0.0"
}

group = "com.github.mintychochip"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
}

java {
    withSourcesJar()
    withJavadocJar()
}

mavenPublishing {
    publishToMavenCentral()
    pom {
        name.set("pb-api")
        description.set("PacketBlocks API")
        url.set("https://github.com/mintychochip/PacketBlocks")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        scm {
            url.set("https://github.com/mintychochip/PacketBlocks")
            connection.set("scm:git:https://github.com/mintychochip/PacketBlocks.git")
            developerConnection.set("scm:git:ssh://[email protected]:mintychochip/PacketBlocks.git")
        }
        developers {
            developer {
                id.set("mintychochip")
                name.set("mintychochip")
                email.set("[email protected]")
            }
        }
    }
}

nmcp {
    publishAllPublicationsToCentralPortal {
        username.set(
            providers.gradleProperty("mavenCentralUsername")
                .orElse(providers.environmentVariable("MAVEN_CENTRAL_USERNAME"))
        )
        password.set(
            providers.gradleProperty("mavenCentralPassword")
                .orElse(providers.environmentVariable("MAVEN_CENTRAL_PASSWORD"))
        )
        publishingType.set("AUTOMATIC")
    }
}
