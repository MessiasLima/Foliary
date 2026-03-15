package dev.appoutlet.foliary.data.authentication.mapper

import dev.appoutlet.foliary.data.authentication.model.Session
import io.github.jan.supabase.auth.user.UserSession
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SessionMapperTest {
    private val mapper = SessionMapper()

    @Test
    fun `should map UserSession to Session`() {
        val userSession = UserSession(
            accessToken = "access_token",
            refreshToken = "refresh_token",
            providerRefreshToken = "provider_refresh_token",
            providerToken = "provider_token",
            expiresIn = 3600L,
            tokenType = "Bearer",
            type = "authenticated",
            user = null
        )

        val result = mapper(userSession)

        result.accessToken shouldBe userSession.accessToken
        result.refreshToken shouldBe userSession.refreshToken
        result.providerRefreshToken shouldBe userSession.providerRefreshToken
        result.providerToken shouldBe userSession.providerToken
        result.expiresIn shouldBe userSession.expiresIn
        result.tokenType shouldBe userSession.tokenType
        result.type shouldBe userSession.type
    }
}
