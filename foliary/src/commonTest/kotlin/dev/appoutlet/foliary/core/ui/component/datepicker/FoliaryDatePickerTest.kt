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
import foliary.foliary.generated.resources.date_picker_confirm
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.time.Instant

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
        val selectedDate = Instant.parse("2026-07-21T12:00:00Z")
        val expectedDateText = selectedDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = selectedDate,
                onDateSelected = {}
            )
        }

        onNodeWithText(expectedDateText).assertIsDisplayed()
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
                onDateSelected = {}
            )
        }

        onNodeWithContentDescription(clearDescription).assertDoesNotExist()
    }

    @Test
    fun `clear button should call onDateSelected with null`() = runComposeUiTest {
        val placeholder = getString(Res.string.create_task_due_date_placeholder)
        val clearDescription = getString(Res.string.create_task_due_date_clear_a11y)
        val selectedDate = Instant.parse("2026-07-21T12:00:00Z")
        var cleared = false

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = selectedDate,
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
        val confirmText = getString(Res.string.date_picker_confirm)

        setContent {
            FoliaryDatePicker(
                label = "Due date",
                placeholder = placeholder,
                selectedDate = null,
                onDateSelected = {}
            )
        }

        onNodeWithTag("FoliaryDatePicker:Card", useUnmergedTree = true).performClick()

        waitForIdle()

        onNodeWithText(confirmText).assertExists()
    }
}
