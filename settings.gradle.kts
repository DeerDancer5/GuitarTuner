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
            repositories {
                maven (url =  "https://www.jitpack.io")
            }
    }
}

rootProject.name = "Tuner"
include(":app")
 