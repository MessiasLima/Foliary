package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliarySecondaryButtonTest {

    @Test
    fun `should show content, be enabled by default and invoke onClick when clicked`() = runComposeUiTest {
        var clicked = false
        val testTag = "SecondaryButton"
        val buttonText = "Click Me"

        setContent {
            FoliarySecondaryButton(
                onClick = { clicked = true },
                modifier = Modifier.testTag(testTag)
            ) {
                Text(buttonText)
            }
        }

        onNodeWithText(buttonText).assertExists()
        onNodeWithTag(testTag).assertIsEnabled().performClick()
        clicked shouldBe true
    }

    @Test
    fun `should be disabled when enabled is false`() = runComposeUiTest {
        var clicked = false
        val testTag = "SecondaryButton"

        setContent {
            FoliarySecondaryButton(
                onClick = { clicked = true },
                enabled = false,
                modifier = Modifier.testTag(testTag)
            ) {
                Text("Disabled")
            }
        }

        onNodeWithTag(testTag).assertIsNotEnabled()
        onNodeWithTag(testTag).performClick()
        clicked shouldBe false
    }
}
