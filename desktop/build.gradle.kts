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
}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "Foliary"
//            packageVersion = libs.versions.versionName.get()
//
//            linux {
//                iconFile.set(project.file("appIcons/LinuxIcon.png"))
//            }
//
//            windows {
//                iconFile.set(project.file("appIcons/WindowsIcon.ico"))
//            }
//
//            macOS {
//                iconFile.set(project.file("appIcons/MacosIcon.icns"))
//                bundleID = "dev.appoutlet.foliary"
//            }
//        }
//    }
//}

nucleus.application {
    mainClass = "MainKt"

    nativeDistributions {
        targetFormats(TargetFormat.Dmg, TargetFormat.Nsis, TargetFormat.Deb)
        packageName = "Foliary"
        packageVersion = libs.versions.versionName.get()
        protocol("Foliary", "foliary")
    }
}

apply(from = "$rootDir/config/detekt/detekt.gradle")