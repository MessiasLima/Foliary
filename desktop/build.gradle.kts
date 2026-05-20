import io.github.kdroidfilter.nucleus.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.nucleus)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xskip-prerelease-check"
        )
    }
}

dependencies {
    implementation(project(":foliary"))
    implementation(compose.desktop.currentOs)
    implementation(libs.nucleus.core.runtime)
    implementation(libs.nucleus.aot.runtime)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions)
}

nucleus.application {
    mainClass = "MainKt"

    nativeDistributions {
        targetFormats(
            TargetFormat.Dmg,
            TargetFormat.Nsis,
            TargetFormat.Msi,
            TargetFormat.Deb,
            TargetFormat.AppImage
        )
        packageName = "Foliary"
        packageVersion = libs.versions.versionName.get()
        protocol("Foliary", "foliary")

        enableAotCache = true

        macOS {
            bundleID = "dev.appoutlet.foliary"
            signing {
                sign.set(false) // Set to true and provide identity for real signing
            }
        }

        windows {
            signing {
                enabled = false // Set to true and provide certificate for real signing
            }
        }
    }
}

apply(from = "$rootDir/config/detekt/detekt.gradle")
