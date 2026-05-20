package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.data.authentication.database.SessionDao
import dev.appoutlet.foliary.data.authentication.mapper.SessionMapper
import dev.appoutlet.foliary.data.authentication.mapper.UserSessionMapper
import dev.appoutlet.foliary.data.authentication.model.Session
import dev.appoutlet.foliary.data.authentication.model.fixture
import dev.appoutlet.foliary.data.authentication.model.userSessionFixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class DefaultAuthenticationRepositoryTest {
    val mockAuth = mock<Auth>(mode = MockMode.autoUnit)
    val mockSessionDao = mock<SessionDao>(mode = MockMode.autoUnit)
    val mockUserSessionMapper = mock<UserSessionMapper>()
    val mockSessionMapper = mock<SessionMapper>()

    val subject = DefaultAuthenticationRepository(
        auth = mockAuth,
        sessionDao = mockSessionDao,
        userSessionMapper = mockUserSessionMapper,
        sessionMapper = mockSessionMapper
    )

    @Test
    fun `should return session status`() = runTest {
        val fixtureSessionStatus = SessionStatus.Initializing

        everySuspend { mockAuth.sessionStatus } returns MutableStateFlow(fixtureSessionStatus)

        val actual = subject.sessionStatus()

        actual.value shouldBe fixtureSessionStatus
    }

    @Test
    fun `should delete session`() = runTest {
        subject.deleteSession()

        verifySuspend { mockSessionDao.deleteAll() }
    }

    @Test
    fun `should load session`() = runTest {
        val fixtureSession = Session.fixture()
        val fixtureUserSession = userSessionFixture()

        everySuspend { mockSessionDao.load() } returns fixtureSession
        every { mockUserSessionMapper(fixtureSession) } returns fixtureUserSession

        val result = subject.loadSession()

        result shouldBe fixtureUserSession
    }

    @Test
    fun `should throw when loading session and none exists`() = runTest {
        everySuspend { mockSessionDao.load() } returns null

        shouldThrow<IllegalArgumentException> { subject.loadSession() }
    }

    @Test
    fun `should save session`() = runTest {
        val fixtureUserSession = userSessionFixture()
        val fixtureSession = Session.fixture()

        every { mockSessionMapper(fixtureUserSession) } returns fixtureSession

        subject.saveSession(fixtureUserSession)

        verifySuspend { mockSessionDao.insert(fixtureSession) }
    }

    @Test
    fun `should sign out`() = runTest {
        subject.signOut()

        verifySuspend { mockAuth.signOut() }
    }

    @Test
    fun `should import auth token`() = runTest {
        val fixtureAccessToken = "access_token"
        val fixtureRefreshToken = "refresh_token"

        subject.importAuthToken(fixtureAccessToken, fixtureRefreshToken)

        verifySuspend {
            mockAuth.importAuthToken(fixtureAccessToken, fixtureRefreshToken, retrieveUser = true, autoRefresh = true)
        }
    }
}
