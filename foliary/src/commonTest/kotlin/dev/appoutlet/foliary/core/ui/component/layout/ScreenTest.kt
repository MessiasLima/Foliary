package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.analytics.MockAnalytics
import dev.appoutlet.foliary.core.mvi.State
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ScreenTest {
    @Test
    fun `should start with idle state`() = runComposeUiTest {
        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { SampleViewModel(State.Idle) },
                idle = { Text("Idle screen") },
                content = { viewData: SampleViewData -> }
            )
        }

        onNodeWithText("Idle screen").assertExists()
    }

    @Test
    fun `should show loading state`() = runComposeUiTest {
        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { SampleViewModel(State.Loading()) },
                loading = { Text("Loading screen") },
                content = { _: SampleViewData -> }
            )
        }

        onNodeWithText("Loading screen").assertExists()
    }

    @Test
    fun `should show error state`() = runComposeUiTest {
        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { SampleViewModel(State.Error(Exception("Error message"))) },
                error = { Text("Error screen: ${it?.message}") },
                content = { _: SampleViewData -> }
            )
        }

        onNodeWithText("Error screen: Error message").assertExists()
    }

    @Test
    fun `should show content state`() = runComposeUiTest {
        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { SampleViewModel(State.Success(SampleViewData)) },
                content = { _: SampleViewData -> Text("Content screen") }
            )
        }

        onNodeWithText("Content screen").assertExists()
    }

    @Test
    fun `should track screen view with analytics`() = runComposeUiTest {
        // Given a mock analytics instance
        val mockAnalytics = MockAnalytics()

        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                Screen(
                    screenName = "TestScreen",
                    viewModelProvider = { SampleViewModel(State.Idle) },
                    idle = { Text("Idle screen") },
                    content = { _: SampleViewData -> }
                )
            }
        }

        // Wait for the screen to render
        waitForIdle()

        // Then screen should be tracked
        mockAnalytics.screenViews shouldHaveSize 1
        mockAnalytics.screenViews[0] shouldBe "TestScreen"
    }
}
