package dev.appoutlet.foliary.core.ui.component.datepicker

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_due_date_clear_a11y
import foliary.foliary.generated.resources.create_task_due_date_placeholder
import foliary.foliary.generated.resources.general_confirm
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliaryDatePickerTest {

    @Test
    fun `should show placeholder when selectedDate is null`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = null,
                selectedDateDisplayText = null,
                onDateSelected = {}
            )
        }

        onNodeWithText(placeholder).assertIsDisplayed()
        onNodeWithTag("FoliaryDatePicker:Value", useUnmergedTree = true).assertIsDisplayed()
        onNodeWithTag("FoliaryDatePicker:ClearButton", useUnmergedTree = true).assertDoesNotExist()
    }

    @Test
    fun `should show selected date when selectedDate is not null`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val selectedDateDisplayText = "2026-07-21"

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = 1_752_960_000_000,
                selectedDateDisplayText = selectedDateDisplayText,
                onDateSelected = {}
            )
        }

        onNodeWithText(selectedDateDisplayText).assertIsDisplayed()
        onNodeWithTag("FoliaryDatePicker:Value", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `should show clear button only when date is selected`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val clearDescription = getString(Res.string.create_task_due_date_clear_a11y)

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = null,
                selectedDateDisplayText = null,
                onDateSelected = {}
            )
        }

        onNodeWithContentDescription(clearDescription).assertDoesNotExist()
    }

    @Test
    fun `clear button should call onDateSelected with null`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val clearDescription = getString(Res.string.create_task_due_date_clear_a11y)
        var cleared = false

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = 1_752_960_000_000,
                selectedDateDisplayText = "2026-07-21",
                onDateSelected = { cleared = it == null }
            )
        }

        onNodeWithContentDescription(clearDescription)
            .assertIsDisplayed()
            .performClick()

        cleared shouldBe true
    }

    @Test
    fun `should open date picker bottom sheet when card is clicked`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val confirmText = getString(Res.string.general_confirm)

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = null,
                selectedDateDisplayText = null,
                onDateSelected = {}
            )
        }

        onNodeWithTag("FoliaryDatePicker:Card", useUnmergedTree = true).performClick()

        waitForIdle()

        onNodeWithText(confirmText).assertExists()
    }

    @Test
    fun `should not open date picker bottom sheet when clear button is clicked`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val clearDescription = getString(Res.string.create_task_due_date_clear_a11y)
        val confirmText = getString(Res.string.general_confirm)

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = 1_752_960_000_000,
                selectedDateDisplayText = "2026-07-21",
                onDateSelected = {}
            )
        }

        onNodeWithContentDescription(clearDescription)
            .assertIsDisplayed()
            .performClick()

        waitForIdle()

        onNodeWithText(confirmText).assertDoesNotExist()
    }
}
