package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ErrorIndicatorTest {

    @Test
    fun `should show all elements`() = runComposeUiTest {
        val title = "Error Title"
        val message = "Error Message"
        val tryAgainText = "Retry"
        val stackTrace = "My Stack Trace"
        var tryAgainClicked = false

        setContent {
            ErrorIndicator(
                title = title,
                message = message,
                onTryAgain = { tryAgainClicked = true },
                tryAgainText = tryAgainText,
                stackTrace = stackTrace
            )
        }

        // Verify Title and Message
        onNodeWithTag("ErrorIndicator:Icon").assertIsDisplayed()
        onNodeWithText(title).assertIsDisplayed()
        onNodeWithText(message).assertIsDisplayed()

        // Verify Try Again Button
        onNodeWithText(tryAgainText).assertIsDisplayed().performClick()
        tryAgainClicked shouldBe true

        // Verify Stack Trace behavior
        onNodeWithText(stackTrace).assertDoesNotExist()

        onNodeWithTag("ErrorIndicator:ToggleStackTrace")
            .performClick()

        onNodeWithTag("ErrorIndicator:StackTrace").assertIsDisplayed()
        onNodeWithText(stackTrace).assertIsDisplayed()

        onNodeWithTag("ErrorIndicator:ToggleStackTrace")
            .performClick()

        onNodeWithTag("ErrorIndicator:StackTrace").assertDoesNotExist()
        onNodeWithText(stackTrace).assertDoesNotExist()
    }

    @Test
    fun `should not show title and message when null`() = runComposeUiTest {
        setContent {
            ErrorIndicator(
                title = null,
                message = null,
                onTryAgain = null
            )
        }

        onNodeWithText("Error Title").assertDoesNotExist()
        onNodeWithText("Error Message").assertDoesNotExist()
    }

    @Test
    fun `should not show try again button when null`() = runComposeUiTest {
        val tryAgainText = "Retry"

        setContent {
            ErrorIndicator(
                onTryAgain = null,
                tryAgainText = tryAgainText
            )
        }

        onNodeWithText(tryAgainText).assertDoesNotExist()
    }

    @Test
    fun `should not show stack trace options when not provided`() = runComposeUiTest {
        val stackTrace = "Stack trace content"

        setContent {
            ErrorIndicator(
                stackTrace = null,
                onTryAgain = null
            )
        }

        onNodeWithText(stackTrace).assertDoesNotExist()
        onNodeWithText("Show details").assertDoesNotExist()
    }
}
