package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import org.koin.core.annotation.Single

@Single
class DefaultAuthenticationRepository(
    private val auth: Auth,
) : AuthenticationRepository {
    override suspend fun requestMagicLink(email: String) {
        auth.signInWith(OTP) { email }
    }
}