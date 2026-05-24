package dev.appoutlet.foliary.feature.signin

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.data.authentication.model.userSessionFixture
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.auth.user.UserInfo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class SignInViewModelTest : ViewModelTest<SignInViewModel, SignInViewData, SignInAction>() {
    private val sessionStatusFlow = MutableStateFlow<SessionStatus>(SessionStatus.Initializing)
    private val mockAuthenticationRepository = mock<AuthenticationRepository>(MockMode.autoUnit)
        .apply { every { sessionStatus() } returns sessionStatusFlow }

    override fun createViewModel() = SignInViewModel(
        authenticationRepository = mockAuthenticationRepository,
        deeplinkDispatcher = DeepLinkDispatcher,
    )

    @Test
    fun `should have Idle as initial state`() = test {
        viewModel.container.stateFlow.value shouldBe SignInViewData.Idle
    }

    @Test
    fun `should show loading when session is initializing`() = test {
        expectState(SignInViewData.Loading)
    }

    @Test
    fun `should show not authenticated when session is not authenticated`() {
        sessionStatusFlow.value = SessionStatus.NotAuthenticated()

        test {
            expectState(SignInViewData.NotAuthenticated())
        }
    }

    @Test
    fun `should navigate to main when session is already authenticated`() {
        sessionStatusFlow.value = SessionStatus.Authenticated(userSessionFixture())

        test {
            expectSideEffect(SignInAction.NavigateToMain)
        }
    }

    @Test
    fun `should show authenticated before navigating when user signs in`() {
        val email = "user@example.com"
        val session = userSessionFixture().copy(
            user = UserInfo(
                aud = "authenticated",
                id = "user-id",
                email = email,
            )
        )

        sessionStatusFlow.value = SessionStatus.NotAuthenticated()

        test {
            expectState(SignInViewData.NotAuthenticated())

            sessionStatusFlow.value = SessionStatus.Authenticated(
                session = session,
                source = SessionSource.External,
            )

            expectState(SignInViewData.Authenticated(userName = email, newUser = true))
            advanceTimeBy(1.seconds)
            expectSideEffect(SignInAction.NavigateToMain)
        }
    }

    @Test
    fun `should request magic link`() {
        val email = "user@example.com"
        sessionStatusFlow.value = SessionStatus.NotAuthenticated()

        test {
            expectState(SignInViewData.NotAuthenticated())

            viewModel.onEvent(SignInEvent.OnSendMagicLink(email))

            expectState(SignInViewData.NotAuthenticated(requestingMagicLink = true))
            verifySuspend { mockAuthenticationRepository.requestMagicLink(email) }
            expectState(SignInViewData.MagicLinkSent(email))
        }
    }

    @Test
    fun `should return to not authenticated when selecting new email`() = test {
        expectState(SignInViewData.Loading)

        viewModel.onEvent(SignInEvent.OnSelectNewEmail)

        expectState(SignInViewData.NotAuthenticated())
    }
}
