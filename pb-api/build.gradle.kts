plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.vanniktech.maven.publish") version "0.34.0"
    id("com.gradleup.nmcp") version "1.0.0"
}

group = "com.github.yourname"     // your verified namespace
version = "1.0.0"                  // release (use -SNAPSHOT for snapshots)

java {
    withSourcesJar()
    withJavadocJar()
}

mavenPublishing {
    publishToMavenCentral()          // configures Central-ready metadata
    signAllPublications()
    coordinates(group.toString(), project.name, version.toString())
    pom {
        name.set("pb-api")
        description.set("Your high-performance whatever, like Caffeine’s style.")
        url.set("https://github.com/yourname/pb-api")
        licenses {
            license {
                name.set("Apache-2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        scm {
            url.set("https://github.com/yourname/pb-api")
            connection.set("scm:git:https://github.com/yourname/pb-api.git")
            developerConnection.set("scm:git:ssh://[email protected]:yourname/pb-api.git")
        }
        developers {
            developer {
                id.set("you"); name.set("Your Name"); email.set("you@example.com")
            }
        }
    }
}

nmcp {
    publishAllPublicationsToCentralPortal {
        // AUTOMATIC ⇒ validates then publishes; USER_MANAGED ⇒ you click Publish in the Portal UI
        publishingType.set("AUTOMATIC")
    }
}

signing {
    useInMemoryPgpKeys(
        providers.environmentVariable("SIGNING_KEY").orNull,
        providers.environmentVariable("SIGNING_PASSWORD").orNull
    )
    sign(publishing.publications)
}
