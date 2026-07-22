package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.core.allopen.Open
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.user.UserSession
import org.koin.core.annotation.Single

@Single
@Open
class DefaultAuthenticationRepository(
    lazyAuth: Lazy<Auth>,
    ksafe: KSafe,
) : AuthenticationRepository {
    private var userSession by ksafe<UserSession?>(defaultValue = null, key = "session")

    private val auth by lazyAuth

    override fun sessionStatus() = auth.sessionStatus

    override suspend fun requestMagicLink(email: String) {
        auth.signInWith(provider = OTP, redirectUrl = getRedirectUrl()) {
            this.email = email
        }
    }

    override suspend fun importAuthToken(accessToken: String, refreshToken: String) {
        auth.importAuthToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
            retrieveUser = true,
            autoRefresh = true
        )
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteSession() {
        userSession = null
    }

    override suspend fun loadSession(): UserSession {
        return requireNotNull(userSession)
    }

    override suspend fun saveSession(session: UserSession) {
        userSession = session
    }

    override fun currentUser() = auth.currentUserOrNull()
}

expect fun getRedirectUrl(): String
