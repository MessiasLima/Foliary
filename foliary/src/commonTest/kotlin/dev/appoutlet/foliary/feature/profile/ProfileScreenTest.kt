package dev.appoutlet.foliary.feature.profile

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.analytics.MockAnalytics
import dev.appoutlet.foliary.core.navigation.LocalNavigator
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import dev.mokkery.MockMode
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.profile_logout
import foliary.foliary.generated.resources.profile_statistics
import foliary.foliary.generated.resources.profile_title
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ProfileScreenTest {
    private val mockAnalytics = MockAnalytics()
    private val mockAuthenticationRepository = mock<AuthenticationRepository>(mode = MockMode.autoUnit)

    @Test
    fun `should render profile screen with options`() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                ProfileScreenContent(
                    lazyListState = rememberLazyListState(),
                    viewModel = ProfileViewModel(mockAuthenticationRepository, mockAnalytics),
                )
            }
        }

        onNodeWithText(getString(Res.string.profile_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.profile_statistics)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.profile_logout)).assertIsDisplayed()
    }

    @Test
    fun `should track statistics click`() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                ProfileScreenContent(
                    lazyListState = rememberLazyListState(),
                    viewModel = ProfileViewModel(mockAuthenticationRepository, mockAnalytics),
                )
            }
        }

        onNodeWithText(getString(Res.string.profile_statistics)).performClick()

        waitUntil("Statistics click is tracked") {
            mockAnalytics.events.containsKey("profile_statistics_clicked")
        }
    }

    @Test
    fun `should log out and navigate to sign in`() = runComposeUiTest {
        val fakeNavigator = FakeNavigator()

        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                CompositionLocalProvider(LocalNavigator provides fakeNavigator) {
                    ProfileScreenContent(
                        lazyListState = rememberLazyListState(),
                        viewModel = ProfileViewModel(mockAuthenticationRepository, mockAnalytics),
                    )
                }
            }
        }

        onNodeWithText(getString(Res.string.profile_logout)).performClick()

        waitUntil("Navigated to sign in") { fakeNavigator.roots.isNotEmpty() }

        verifySuspend { mockAuthenticationRepository.signOut() }
        mockAnalytics.events.containsKey("user_logged_out") shouldBe true
        fakeNavigator.roots shouldBe listOf(SignInNavKey)
    }
}

private class FakeNavigator : Navigator {
    val roots = mutableListOf<NavKey>()

    override fun navigate(destination: NavKey) = Unit

    override fun setRoot(destination: NavKey) {
        roots.add(destination)
    }

    override fun goBack() = Unit
}
