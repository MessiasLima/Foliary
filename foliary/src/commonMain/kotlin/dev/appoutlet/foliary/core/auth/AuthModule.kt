package dev.appoutlet.foliary.core.auth

import dev.appoutlet.foliary.BuildKonfig
import io.github.jan.tennert.supabase.SupabaseClient
import io.github.jan.tennert.supabase.auth.Auth
import io.github.jan.tennert.supabase.auth.auth
import io.github.jan.tennert.supabase.createSupabaseClient
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
