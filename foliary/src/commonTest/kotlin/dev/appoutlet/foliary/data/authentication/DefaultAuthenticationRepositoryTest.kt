package dev.appoutlet.foliary.data.authentication

import dev.appoutlet.foliary.data.authentication.model.userSessionFixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import eu.anifantakis.lib.ksafe.KSafe
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.builtin.OTP
import io.github.jan.supabase.auth.status.SessionStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlin.io.path.createTempDirectory
import kotlin.test.Test

class DefaultAuthenticationRepositoryTest {
    private val mockAuth = mock<Auth>(mode = MockMode.autoUnit)
    private val ksafe = KSafe(baseDir = createTempDirectory("ksafe-test").toFile())

    val subject = DefaultAuthenticationRepository(
        lazyAuth = lazy { mockAuth },
        ksafe = ksafe,
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
        val fixtureUserSession = userSessionFixture()
        subject.saveSession(fixtureUserSession)

        subject.deleteSession()

        shouldThrow<IllegalArgumentException> { subject.loadSession() }
    }

    @Test
    fun `should load session`() = runTest {
        val fixtureUserSession = userSessionFixture()

        subject.saveSession(fixtureUserSession)

        val result = subject.loadSession()

        result shouldBe fixtureUserSession
    }

    @Test
    fun `should throw when loading session and none exists`() = runTest {
        shouldThrow<IllegalArgumentException> { subject.loadSession() }
    }

    @Test
    fun `should save session`() = runTest {
        val fixtureUserSession = userSessionFixture()

        subject.saveSession(fixtureUserSession)

        val result = subject.loadSession()
        result shouldBe fixtureUserSession
    }

    @Test
    fun `should sign out`() = runTest {
        subject.signOut()

        verifySuspend { mockAuth.signOut() }
    }

    @Test
    fun `should request magic link`() = runTest {
        val fixtureEmail = "test@example.com"

        subject.requestMagicLink(fixtureEmail)

        verifySuspend {
            mockAuth.signInWith(provider = OTP, redirectUrl = getRedirectUrl(), config = any())
        }
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
