package dev.appoutlet.foliary.core.supabase

import dev.appoutlet.foliary.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class SupabaseModule {
    @Single
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildKonfig.supabaseUrl,
            supabaseKey = BuildKonfig.supabasePublishableKey,
        ) {
            install(Auth)
        }
    }
}
