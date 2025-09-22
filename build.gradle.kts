plugins {
    `maven-publish`
    id("com.gradleup.nmcp") version "1.0.0"
    // you do NOT need to apply `maven-publish` here if your publishing module (pb-api)
    // already applies it via Vanniktech (it does).
}

nmcp {
    publishAllPublicationsToCentralPortal {
        // Wire creds from -P OR env so Nmcp always sees them
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

tasks.register("debugCentralCreds") {
    doLast {
        println("mavenCentralUsername = " + (findProperty("mavenCentralUsername") ?: "<null>"))
        println("mavenCentralPassword = " + (if (findProperty("mavenCentralPassword") == null) "<null>" else "<present>"))
    }
}
