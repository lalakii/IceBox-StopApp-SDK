pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}
@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}
rootProject.name = "IceBox&StopApp-SDK"
include(":sample")
include(":sdk")
