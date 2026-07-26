package dev.appoutlet.foliary.data.authentication

import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository : SessionManager {
    fun sessionStatus(): StateFlow<SessionStatus>
    fun currentUser(): UserInfo?
    suspend fun requestMagicLink(email: String)
    suspend fun importAuthToken(accessToken: String, refreshToken: String)
    suspend fun signOut()
}
