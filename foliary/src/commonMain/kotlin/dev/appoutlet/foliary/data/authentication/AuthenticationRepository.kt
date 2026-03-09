package dev.appoutlet.foliary.data.authentication

interface AuthenticationRepository {
    suspend fun requestMagicLink(email: String)
}
