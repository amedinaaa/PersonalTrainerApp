pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Open source library - Compose Form: https://betterprogramming.pub/advanced-forms-in-android-with-compose-form-154ee0bff65b
//        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "FitnessPage"
include(":app")
 