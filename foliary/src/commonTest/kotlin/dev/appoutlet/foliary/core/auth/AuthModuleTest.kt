package dev.appoutlet.foliary.core.auth

import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.auth.Auth
import io.kotest.matchers.shouldNotBe
import kotlin.test.AfterTest
import kotlin.test.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.ksp.generated.module
import org.koin.test.KoinTest
import org.koin.test.inject

class AuthModuleTest : KoinTest {
    private val supabaseClient: SupabaseClient by inject()
    private val auth: Auth by inject()

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should provide SupabaseClient and Auth`() {
        startKoin {
            modules(AuthModule().module)
        }

        supabaseClient shouldNotBe null
        auth shouldNotBe null
    }
}
