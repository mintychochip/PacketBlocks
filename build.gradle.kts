plugins {
    `maven-publish` // add this
    id("com.gradleup.nmcp") version "1.0.0"
}

nmcp {
    publishAllPublicationsToCentralPortal {
        publishingType.set("AUTOMATIC")
    }
}
