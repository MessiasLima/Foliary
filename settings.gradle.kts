import kotlinx.kover.gradle.aggregation.settings.dsl.minBound

rootProject.name = "Foliary"

pluginManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content { 
              	includeGroupByRegex("com\\.android.*")
              	includeGroupByRegex("com\\.google.*")
              	includeGroupByRegex("androidx.*")
              	includeGroupByRegex("android.*")
            }
        }
        mavenCentral()
    }
}

include(":foliary")
include(":android")
include(":desktop")

plugins {
    id("org.jetbrains.kotlinx.kover.aggregation") version "0.9.7"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

kover {
    enableCoverage()
    reports {
        excludedClasses = listOf(
            // Untestable framework code
            "MainKt",
            "ComposableSingletons*",
            "*.ComposableSingletons*",
            "*.MainActivity",
            "*.generated.resources.*",

            // Dependency injection
            "*.FoliaryKoinApplication",

            // Database setup
            "*.FoliaryDatabase*",
            "*.InMemoryDatabaseBuilder*",

            // Logging
            "*.InitSentry*",
            "*.SentryLogWriter*",
        )

        excludesAnnotatedBy = listOf(
            "org.koin.core.annotation.Module",
        )

        verify {
            rule {
                name = "Minimum coverage"
                minBound(80)
            }
        }
    }
}