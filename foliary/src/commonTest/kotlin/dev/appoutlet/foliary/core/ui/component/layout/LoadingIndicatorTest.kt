package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class LoadingIndicatorTest {

    @Test
    fun `should show all elements`() = runComposeUiTest {
        val message = "Loading data"

        setContent {
            LoadingIndicator(message = message)
        }

        onNodeWithTag("LoadingIndicator:Progress").assertIsDisplayed()
        onNodeWithText(message).assertIsDisplayed()
    }

    @Test
    fun `should not show message when null`() = runComposeUiTest {
        setContent {
            LoadingIndicator(message = null)
        }

        onNodeWithTag("LoadingIndicator:Progress").assertIsDisplayed()
        onNodeWithText("Loading data").assertDoesNotExist()
    }
}
