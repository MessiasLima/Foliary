package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.data.authentication.database.SessionDao
import dev.appoutlet.foliary.data.authentication.mapper.SessionMapper
import dev.appoutlet.foliary.data.authentication.mapper.UserSessionMapper
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.user.UserSession
import org.koin.core.annotation.Single

@Single
class DefaultAuthenticationRepository(
    private val auth: Auth,
    private val sessionDao: SessionDao,
    private val userSessionMapper: UserSessionMapper,
    private val sessionMapper: SessionMapper
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

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun deleteSession() {
        sessionDao.deleteAll()
    }

    override suspend fun loadSession(): UserSession? {
        val session = sessionDao.load()
        return session?.let {
            userSessionMapper(it)
        }
    }

    override suspend fun saveSession(session: UserSession) {
        val session = sessionMapper(session)
        sessionDao.insert(session)
    }
}

expect fun getRedirectUrl(): String
