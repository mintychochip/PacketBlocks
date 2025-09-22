plugins {
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(providers.gradleProperty("ossrhUsername").orElse(System.getenv("OSSRH_USERNAME")))
            password.set(providers.gradleProperty("ossrhPassword").orElse(System.getenv("OSSRH_PASSWORD")))
        }
    }
}
