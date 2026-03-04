package dev.appoutlet.foliary.core.auth

import dev.appoutlet.foliary.BuildKonfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.createSupabaseClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class AuthModule {
    @Single
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = BuildKonfig.supabaseUrl,
            supabaseKey = BuildKonfig.supabaseAnonKey,
        ) {
            install(Auth)
        }
    }

    @Single
    fun provideAuth(client: SupabaseClient): Auth = client.auth
}
