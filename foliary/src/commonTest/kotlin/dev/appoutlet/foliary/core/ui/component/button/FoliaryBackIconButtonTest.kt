package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliaryBackIconButtonTest {
    @Test
    fun `should emit when button is clicked`() = runComposeUiTest {
        var clicked = false

        setContent {
            FoliaryBackIconButton(
                modifier = Modifier.testTag("BackButton"),
                onClick = { clicked = true },
            )
        }

        onNodeWithTag("BackButton")
            .assertIsDisplayed()
            .performClick()

        clicked shouldBe true
    }
}
