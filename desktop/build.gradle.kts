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