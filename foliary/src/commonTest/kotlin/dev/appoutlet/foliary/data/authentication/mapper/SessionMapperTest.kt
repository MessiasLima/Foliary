package dev.appoutlet.foliary.data.authentication.mapper

import dev.appoutlet.foliary.data.authentication.model.userSessionFixture
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class SessionMapperTest {
    private val mapper = SessionMapper()

    @Test
    fun `should map UserSession to Session`() {
        val userSession = userSessionFixture()

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
