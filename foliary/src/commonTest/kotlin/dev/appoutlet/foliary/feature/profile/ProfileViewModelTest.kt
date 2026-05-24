package dev.appoutlet.foliary.feature.profile

import dev.appoutlet.foliary.core.analytics.Analytics
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val mockAuthenticationRepository = mock<AuthenticationRepository>(mode = MockMode.autoUnit)
    private val mockAnalytics = mock<Analytics>(mode = MockMode.autoUnit)

    private val viewModel = ProfileViewModel(mockAuthenticationRepository, mockAnalytics)

    @Test
    fun `should load profile screen`() = runTest {
        val viewData = viewModel.container.stateFlow.value
        viewData shouldBe ProfileViewData
    }

    @Test
    fun `should log out`() = runTest {
        viewModel.onEvent(ProfileEvent.OnLogOutClick)

        advanceUntilIdle()

        verifySuspend { mockAuthenticationRepository.signOut() }
        verifySuspend { mockAnalytics.trackEvent("user_logged_out") }

        viewModel.container.sideEffectFlow.first() shouldBe ProfileAction.NavigateToSignIn
    }
}
