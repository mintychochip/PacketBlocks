plugins {
    `maven-publish`                    // ✅ add this on root
    id("com.gradleup.nmcp") version "1.0.0"
}

nmcp {
    publishAllPublicationsToCentralPortal {
        publishingType.set("AUTOMATIC")
    }
}
