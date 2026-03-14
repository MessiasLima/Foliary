package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository : SessionManager{
    suspend fun sessionStatus(): StateFlow<SessionStatus>
    suspend fun requestMagicLink(email: String)
    suspend fun requestGoogleAuthentication()
    suspend fun importAuthToken(accessToken: String, refreshToken: String)
    suspend fun signOut()
}
