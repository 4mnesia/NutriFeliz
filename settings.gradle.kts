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
    // ⬅️ El bloque "plugins" NO va aquí.
}

// --- CORRECCIÓN: El bloque va aquí, al nivel principal ---
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
// --- FIN DE LA CORRECCIÓN ---

rootProject.name = "NutriApp" // (Tu proyecto se llamaba NutriFeliz antes, pero esto está bien)
include(":app")