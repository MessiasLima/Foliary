package dev.appoutlet.foliary.feature.signin

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import dev.appoutlet.foliary.feature.common.deeplink.fixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.status.SessionStatus
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
    fun `should import auth token from deeplink`() = test {
        val deeplink = Deeplink.fixture(
            queryParameters = mapOf(
                "access_token" to "access-token-value",
                "refresh_token" to "refresh-token-value",
            )
        )

        DeepLinkDispatcher.dispatch(deeplink)

        advanceTimeBy(1.seconds)

        verifySuspend {
            mockAuthenticationRepository.importAuthToken(
                "access-token-value",
                "refresh-token-value"
            )
        }
    }

    @Test
    fun `should not import auth token when deeplink is missing tokens`() = test {
        val deeplink = Deeplink.fixture(
            queryParameters = mapOf("other" to "param")
        )

        DeepLinkDispatcher.dispatch(deeplink)
        advanceUntilIdle()

        verifySuspend(mode = VerifyMode.exactly(0)) {
            mockAuthenticationRepository.importAuthToken(any(), any())
        }
    }
}
