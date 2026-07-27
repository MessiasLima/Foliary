package dev.appoutlet.foliary.feature.createtask

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.back_icon_button_a11y
import foliary.foliary.generated.resources.create_task_description_label
import foliary.foliary.generated.resources.create_task_description_placeholder
import foliary.foliary.generated.resources.create_task_due_date_clear_a11y
import foliary.foliary.generated.resources.create_task_due_date_label
import foliary.foliary.generated.resources.create_task_due_date_placeholder
import foliary.foliary.generated.resources.create_task_save
import foliary.foliary.generated.resources.create_task_title
import foliary.foliary.generated.resources.create_task_title_label
import foliary.foliary.generated.resources.create_task_title_placeholder
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.time.Instant

@OptIn(ExperimentalTestApi::class)
class CreateTaskScreenTest {
    @Test
    fun `should render create task form`() = runComposeUiTest {
        setContent {
            CreateTaskScreen(viewData = CreateTaskViewData(), onEvent = {})
        }

        onNodeWithText(getString(Res.string.create_task_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_title_label)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_title_placeholder)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_description_label)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_description_placeholder)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_due_date_label)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_due_date_placeholder)).assertIsDisplayed()
        onNodeWithContentDescription(getString(Res.string.create_task_due_date_clear_a11y))
            .assertDoesNotExist()
        onNodeWithText(getString(Res.string.create_task_save)).assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun `should emit events when actions are clicked`() = runComposeUiTest {
        val events = mutableListOf<CreateTaskEvent>()

        setContent {
            CreateTaskScreen(
                viewData = CreateTaskViewData(saveButtonEnabled = true),
                onEvent = events::add,
            )
        }

        onNodeWithContentDescription(getString(Res.string.back_icon_button_a11y))
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(getString(Res.string.create_task_save))
            .assertIsEnabled()
            .performClick()

        events.contains(CreateTaskEvent.BackClicked) shouldBe true
        events.contains(CreateTaskEvent.SaveClicked) shouldBe true
    }

    @Test
    fun `should update text fields`() = runComposeUiTest {
        val title = "Buy soil"
        val description = "Use the garden store nearby"

        setContent {
            CreateTaskScreen(viewData = CreateTaskViewData(), onEvent = {})
        }

        onNodeWithTag("CreateTaskScreen:TitleField", useUnmergedTree = true)
            .performTextInput(title)
        onNodeWithTag("CreateTaskScreen:DescriptionField", useUnmergedTree = true)
            .performTextInput(description)

        onNodeWithTag("CreateTaskScreen:TitleField", useUnmergedTree = true)
            .assertTextEquals(title)
        onNodeWithTag("CreateTaskScreen:DescriptionField", useUnmergedTree = true)
            .assertTextEquals(description)
    }

    @Test
    fun `should show selected due date and clear it`() = runComposeUiTest {
        val dueDate = Instant.parse("2026-07-21T12:00:00Z")
        val dueDateMillis = dueDate.toEpochMilliseconds()
        val expectedDateText = dueDate.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        val events = mutableListOf<CreateTaskEvent>()
        var viewData by mutableStateOf(CreateTaskViewData())

        setContent {
            CreateTaskScreen(viewData = viewData, onEvent = events::add)
        }

        onNodeWithText(getString(Res.string.create_task_due_date_label)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.create_task_due_date_placeholder)).assertIsDisplayed()
        onNodeWithContentDescription(getString(Res.string.create_task_due_date_clear_a11y))
            .assertDoesNotExist()

        viewData = CreateTaskViewData(
            dueDate = CreateTaskViewData.DueDateViewData(
                selectedDateMillis = dueDateMillis,
                selectedDateDisplayText = expectedDateText,
            )
        )
        waitForIdle()

        onNodeWithText(expectedDateText).assertIsDisplayed()
        onNodeWithContentDescription(getString(Res.string.create_task_due_date_clear_a11y))
            .assertIsDisplayed()
            .performClick()

        events.contains(CreateTaskEvent.DueDateChanged(null)) shouldBe true

        viewData = CreateTaskViewData()
        waitForIdle()

        onNodeWithText(getString(Res.string.create_task_due_date_placeholder)).assertIsDisplayed()
        onNodeWithContentDescription(getString(Res.string.create_task_due_date_clear_a11y))
            .assertDoesNotExist()
    }
}
