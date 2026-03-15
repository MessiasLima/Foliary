package dev.appoutlet.foliary.data.authentication.mapper

import dev.appoutlet.foliary.data.authentication.model.Session
import dev.appoutlet.foliary.data.authentication.model.fixture
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class UserSessionMapperTest {
    private val mapper = UserSessionMapper()

    @Test
    fun `should map Session to UserSession`() {
        val session = Session.fixture()

        val result = mapper(session)

        result.accessToken shouldBe session.accessToken
        result.refreshToken shouldBe session.refreshToken
        result.providerRefreshToken shouldBe session.providerRefreshToken
        result.providerToken shouldBe session.providerToken
        result.expiresIn shouldBe session.expiresIn
        result.tokenType shouldBe session.tokenType
        result.type shouldBe session.type
    }
}
