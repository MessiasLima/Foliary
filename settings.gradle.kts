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
}

kover {
    enableCoverage()
    reports {
        excludedClasses = listOf(
            "MainKt",
            "ComposableSingletons*",
            "*.ComposableSingletons*",
            "*.MainActivity",
            "*.generated.resources.*"
        )
        verify {
            rule {
                name = "Minimum coverage"
                minBound(80)
            }
        }
    }
}