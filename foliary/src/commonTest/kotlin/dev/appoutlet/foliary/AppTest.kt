package dev.appoutlet.foliary

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
    @Test
    fun `the application should start` () = runComposeUiTest {
        setContent {
            App()
        }

        onNodeWithText("Hello, Foliary!").assertIsDisplayed()
    }
}