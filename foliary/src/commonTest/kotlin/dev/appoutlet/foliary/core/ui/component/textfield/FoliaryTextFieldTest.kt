package dev.appoutlet.foliary.core.ui.component.textfield

import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import dev.appoutlet.foliary.core.testing.assertIsNotTransparent
import dev.appoutlet.foliary.core.testing.assertIsTransparent
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliaryTextFieldTest {

    @Test
    fun `should show and update value`() = runComposeUiTest {
        val tagInput = "FoliaryTextField:Input"
        val tagPlaceholder = "FoliaryTextField:Placeholder"
        val updatedValue = "Buy fertilizer"
        val value = mutableStateOf("")
        val placeholderText = "Task placeholder"

        setContent {
            FoliaryTextField(
                modifier = Modifier.testTag(tagInput),
                value = value.value,
                onValueChange = { value.value = it },
                placeholder = {
                    Text(text = placeholderText)
                }
            )
        }

        onNodeWithTag(tagPlaceholder, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertIsNotTransparent()

        onNodeWithTag(tagInput, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("")
            .performTextInput(updatedValue)

        onNodeWithTag(tagPlaceholder, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertIsTransparent()

        onNodeWithTag(tagInput, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals(updatedValue)
            .performTextClearance()

        onNodeWithTag(tagPlaceholder, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertIsNotTransparent()
    }
}
