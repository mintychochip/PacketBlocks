plugins {
    `maven-publish`                    // ✅ add this on root
    id("com.gradleup.nmcp") version "1.0.0"
}

nmcp {
    publishAllPublicationsToCentralPortal {
        publishingType.set("AUTOMATIC")
    }
}

tasks.register("debugCentralCreds") {
    doLast {
        println("mavenCentralUsername = " + (findProperty("mavenCentralUsername") ?: "<null>"))
        println("mavenCentralPassword = " + (if (findProperty("mavenCentralPassword") == null) "<null>" else "<present>"))
    }
}
