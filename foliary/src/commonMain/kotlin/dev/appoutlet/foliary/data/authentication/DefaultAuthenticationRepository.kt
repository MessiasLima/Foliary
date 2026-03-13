package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import org.koin.core.annotation.Single

@Single
class DefaultAuthenticationRepository(
    private val auth: Auth,
) : AuthenticationRepository {
    override suspend fun sessionStatus() = auth.sessionStatus

    override suspend fun requestMagicLink(email: String) {
        auth.signInWith(provider = OTP, redirectUrl = getRedirectUrl()) {
            this.email = email
        }
    }

    override suspend fun requestGoogleAuthentication() {
        auth.signInWith(provider = Google, redirectUrl = getRedirectUrl())
    }

    override suspend fun importAuthToken(accessToken: String, refreshToken: String) {
        auth.importAuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            retrieveUser = true,
            autoRefresh = true
        )
    }
}

expect fun getRedirectUrl(): String
