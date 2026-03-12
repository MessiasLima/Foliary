package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.Single

// TODO redirect url by platform
private const val RedirectUrl = "foliary://auth"

@Single
class DefaultAuthenticationRepository(
    private val auth: Auth,
) : AuthenticationRepository {
    override suspend fun sessionStatus() = auth.sessionStatus

    override suspend fun requestMagicLink(email: String) {
        auth.signInWith(provider = OTP, redirectUrl = RedirectUrl) {
            this.email = email
        }
    }

    override suspend fun requestGoogleAuthentication() {
        auth.signInWith(provider = Google, redirectUrl = RedirectUrl)
    }

    override suspend fun importAuthToken(accessToken: String, refreshToken: String) {
        auth.importAuthToken(
            accessToken = accessToken,
            refreshToken= refreshToken,
            retrieveUser = true,
            autoRefresh = true
        )
    }
}
