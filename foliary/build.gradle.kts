import com.codingfeline.buildkonfig.compiler.FieldSpec
import java.util.Properties

plugins {
    alias(libs.plugins.android.kmp.library)
    alias(libs.plugins.buildKonfig)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.mokkery)
    alias(libs.plugins.room)
    alias(libs.plugins.sentry)
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
            implementation(libs.sentry)
            implementation(libs.sqlite.bundled)
            implementation(project.dependencies.platform(libs.supabase.bom))
            implementation(libs.supabase.auth)
            implementation(libs.supabase.kt)
            implementation(libs.umami)

        }


        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.compose.ui.test)
            implementation(libs.kotest.assertions)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.sqlite.bundled)
            implementation(libs.ktor.client.okhttp)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.appdirs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ktor.client.cio)
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.addAll(
                    "-Xexpect-actual-classes",
                    "-Xexplicit-backing-fields"
                )
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspJvm", libs.room.compiler)
}

apply(from = "$rootDir/config/detekt/detekt.gradle")

val props = Properties().apply {
    val propsFile = rootProject.file("local.properties")
    if (propsFile.exists()) {
        propsFile.inputStream().use { load(it) }
    }
}

val Properties.umamiWebsiteId: String
    get() = getProperty("umami.websiteId", "")

val Properties.umamiBaseUrl: String
    get() = getProperty("umami.baseUrl", "")

val Properties.sentryDsn: String
    get() = getProperty("sentry.dsn", "")

val Properties.supabaseUrl: String
    get() = getProperty("supabase.url", "")

val Properties.supabaseAnonKey: String
    get() = getProperty("supabase.anonKey", "")

buildkonfig {
    packageName = "dev.appoutlet.foliary"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "true")
        buildConfigField(FieldSpec.Type.INT, "versionCode", libs.versions.versionCode.get())
        buildConfigField(FieldSpec.Type.STRING, "versionName", libs.versions.versionName.get())
        buildConfigField(FieldSpec.Type.STRING, "versionUuid", libs.versions.versionUuid.get())
        buildConfigField(FieldSpec.Type.STRING, "umamiWebsiteId", props.umamiWebsiteId)
        buildConfigField(FieldSpec.Type.STRING, "umamiBaseUrl", props.umamiBaseUrl)
        buildConfigField(FieldSpec.Type.STRING, "sentryDsn", props.sentryDsn)
        buildConfigField(FieldSpec.Type.STRING, "supabaseUrl", props.supabaseUrl)
        buildConfigField(FieldSpec.Type.STRING, "supabaseAnonKey", props.supabaseAnonKey)
    }

    defaultConfigs("release") {
        buildConfigField(FieldSpec.Type.BOOLEAN, "isDebug", "false")
    }
}
