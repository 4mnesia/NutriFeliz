pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// --- CORRECCIÓN ---
// Este bloque debe ir al nivel raíz, fuera de los anteriores.
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
// --- FIN DE LA CORRECCIÓN ---

rootProject.name = "NutriApp"
include(":app")
