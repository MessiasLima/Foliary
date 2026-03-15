package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.analytics.MockAnalytics
import dev.appoutlet.foliary.core.mvi.ErrorState
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ScreenTest {

    @Test
    fun `should track screen view on launch`() = runComposeUiTest {
        val mockAnalytics = MockAnalytics()
        val screenName = "TestScreen"

        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                Screen(
                    screenName = screenName,
                    viewModelProvider = { SampleViewModel(SampleViewData) },
                    content = { Text("Content") }
                )
            }
        }

        mockAnalytics.screenViews shouldHaveSize 1
        mockAnalytics.screenViews[0] shouldBe screenName
    }

    @Test
    fun `should show content when error state is null`() = runComposeUiTest {
        setContent {
            Text("Simple Text")
        }

        onNodeWithText("Simple Text").assertExists()
    }

    @Test
    fun `should show error indicator when error state is not null`() = runComposeUiTest {
        val viewModel = SampleViewModel(SampleViewData)
        val errorTitle = "Error Title"
        val errorMessage = "Some error message"

        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { viewModel },
                content = { Text("Content") }
            )
        }

        viewModel.setError(
            ErrorState(
                error = Exception(errorMessage),
                message = errorMessage,
                title = errorTitle
            )
        )

        onNodeWithText(errorMessage).assertExists()
        onNodeWithText(errorTitle).assertExists()
        onNodeWithTag("ErrorIndicator:Icon").assertExists()
        onNodeWithText("Content").assertDoesNotExist()
    }

    @Test
    fun `should handle side effects`() = runComposeUiTest {
        val viewModel = SampleViewModel(SampleViewData)
        var actionReceived: SampleAction? = null

        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { viewModel },
                onAction = { action, _ ->
                    actionReceived = action
                },
                content = { Text("Content") }
            )
        }

        viewModel.emitAction(SampleAction)

        waitUntil("Action should be received") {
            actionReceived != null
        }

        actionReceived shouldBe SampleAction
    }
}
