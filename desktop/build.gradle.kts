import io.github.kdroidfilter.nucleus.desktop.application.dsl.SnapConfinement
import io.github.kdroidfilter.nucleus.desktop.application.dsl.SnapGrade
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
            "-Xskip-prerelease-check",
        )
    }
}

dependencies {
    implementation(project(":foliary"))
    implementation(compose.desktop.currentOs)
    implementation(libs.composenativetray)
    implementation(libs.lucideIcons)
    implementation(libs.nucleus.core.runtime)
    implementation(libs.nucleus.aot.runtime)
    implementation(libs.nucleus.decoratedWindow.jbr)
    implementation(libs.nucleus.decoratedWindow.material3)

    testImplementation(kotlin("test"))
    testImplementation(libs.kotest.assertions)
}

nucleus.application {
    mainClass = "MainKt"

    buildTypes {
        release {
            proguard {
                isEnabled = false
                obfuscate = false
                optimize = false
                configurationFiles.from(project.file("proguard-rules.pro"))
            }
        }
    }

    nativeDistributions {
        packageName = "Foliary"
        packageVersion = libs.versions.versionName.get()
        enableAotCache = false

        protocol("Foliary", "foliary")

        targetFormats(
            TargetFormat.Snap,
            TargetFormat.Flatpak,
            TargetFormat.AppX,
            TargetFormat.Dmg
        )

        linux {
            iconFile.set(project.file("appIcons/LinuxIcon.png"))
            shortcut = true
            packageName = "Foliary"
            appRelease = libs.versions.versionCode.get()
            appCategory = "Utility"
            startupWMClass = "dev-appoutlet-Foliary"

            snap {
                confinement = SnapConfinement.Strict
                grade = SnapGrade.Stable
                base = "core26"
            }

            flatpak {
                license.set(rootProject.file("LICENSE"))
            }
        }

        windows {
            iconFile.set(project.file("appIcons/WindowsIcon.ico"))
            upgradeUuid = libs.versions.versionUuid.get()
            console = false
            perUserInstall = true
            appx {  }
        }

        macOS {
            bundleID = "dev.appoutlet.Foliary"
            dockName = "Foliary"
            appCategory = "public.app-category.utilities"
            minimumSystemVersion = "12.0"
            macOsSdkVersion = "26.0"
            iconFile.set(project.file("appIcons/MacosIcon.icns"))
        }
    }
}

apply(from = "$rootDir/config/detekt/detekt.gradle")
