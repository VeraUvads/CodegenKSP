pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
    }
    plugins {
        id("com.google.devtools.ksp") version "1.9.0-1.0.11"
        kotlin("jvm") version "1.5.31"
        kotlin("android") version "1.8.10"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Codegen KSP"
include(":app")
include(":annotation")
include(":processor")
