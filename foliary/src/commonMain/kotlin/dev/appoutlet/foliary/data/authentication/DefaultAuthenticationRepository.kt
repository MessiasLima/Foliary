package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import org.koin.core.annotation.Single

private const val RedirectUrl = "foliary://auth"

@Single
class DefaultAuthenticationRepository(
    private val auth: Auth,
) : AuthenticationRepository {
    override suspend fun requestMagicLink(email: String) {
        auth.signInWith(provider = OTP, redirectUrl = RedirectUrl) {
            this.email = email
        }
    }

    override suspend fun requestGoogleAuthentication() {
        auth.signInWith(provider = Google, redirectUrl = RedirectUrl)
    }
}
