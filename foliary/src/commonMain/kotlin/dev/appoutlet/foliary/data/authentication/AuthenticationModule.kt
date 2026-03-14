package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.core.database.FoliaryDatabase
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
class AuthenticationModule {
    @Single
    fun provideAuth(client: SupabaseClient): Auth = client.auth

    @Single
    fun provideSessionDao(database: FoliaryDatabase) = database.sessionDao()
}
