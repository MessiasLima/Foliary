import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    android {
        namespace = "dev.appoutlet.foliary"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }


    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FoliaryShared"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        commonMain.dependencies {
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.compose.resources)
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.runtime)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.coil)
            implementation(libs.coil.network.ktor)
            implementation(libs.kermit)
            implementation(libs.kermit.koin)
            implementation(libs.koin.annotations)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.navigation3)
            implementation(libs.koin.compose.viewModel)
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.lucideIcons)
            implementation(libs.material3.adaptive.navigation3)
            implementation(libs.multiplatformSettings)
            implementation(libs.navigation3.ui)
            implementation(libs.orbit.compose)
            implementation(libs.orbit.core)
            implementation(libs.orbit.viewModel)
            implementation(libs.room.runtime)
        }

        commonTest.dependencies {
            implementation(libs.compose.ui.test)
            implementation(libs.kotest.assertions)
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

    }
}



room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
        add("kspJvm", this)
    }
}

apply(from = "$rootDir/config/detekt/detekt.gradle")

buildkonfig {
    packageName = "dev.appoutlet.foliary"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "true")
        buildConfigField(FieldSpec.Type.INT, "versionCode", libs.versions.versionCode.get())
        buildConfigField(FieldSpec.Type.STRING, "versionName", libs.versions.versionName.get())
    }

    defaultConfigs("release") {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "false")
    }
}