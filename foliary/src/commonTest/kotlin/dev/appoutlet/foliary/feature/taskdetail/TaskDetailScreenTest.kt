package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.general_cancel
import foliary.foliary.generated.resources.priority_pill_medium
import foliary.foliary.generated.resources.task_detail_delete
import foliary.foliary.generated.resources.task_detail_delete_dialog_confirm
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_1
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_2
import foliary.foliary.generated.resources.task_detail_delete_dialog_title
import foliary.foliary.generated.resources.task_detail_due_date_period
import foliary.foliary.generated.resources.task_detail_edit
import foliary.foliary.generated.resources.task_detail_mark_completed
import foliary.foliary.generated.resources.task_detail_no_description
import foliary.foliary.generated.resources.task_detail_status_completed
import foliary.foliary.generated.resources.task_item_overdue
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TaskDetailScreenTest {
    @Test
    fun `should render loading state`() = runComposeUiTest {
        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loading,
                onEvent = {},
            )
        }

        onNodeWithTag("LoadingIndicator:Progress").assertIsDisplayed()
    }

    @Test
    fun `should render task details`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture()

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = {},
            )
        }

        onNodeWithTag("TaskDetailScreen:Title")
            .assertIsDisplayed()
            .assertTextEquals(task.title)

        onNodeWithTag("TaskDetailScreen:Description")
            .assertIsDisplayed()
            .assertTextEquals(requireNotNull(task.description))

        onNodeWithTag("TaskDetailScreen:CreationDate")
            .assertIsDisplayed()
            .assertTextEquals(task.creationDate)

        onNodeWithTag("TaskDetailScreen:CompletionDate")
            .assertDoesNotExist()

        onNodeWithTag("TaskDetailScreen:DueDate")
            .assertIsDisplayed()
            .assertTextEquals(requireNotNull(task.dueDate))

        onNodeWithTag("TaskDetailScreen:PriorityPill")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_medium))

        onNodeWithTag("TaskDetailScreen:MarkAsComplete")
            .assertIsDisplayed()
    }

    @Test
    fun `should render no description placeholder`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture(description = null)

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = {},
            )
        }

        onNodeWithTag("TaskDetailScreen:Description")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.task_detail_no_description))
    }

    @Test
    fun `should render overdue pill and due date period`() = runComposeUiTest {
        val dueDate = "29 Jul 2026"
        val overduePeriod = getString(Res.string.task_detail_due_date_period, 3)
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture(
            dueDate = dueDate,
            isOverdue = true,
            overduePeriodInDays = 3,
        )

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_item_overdue)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:DueDate")
            .assertIsDisplayed()
            .assertTextEquals("$dueDate · $overduePeriod")
    }

    @Test
    fun `should emit mark completed event`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture(isComplete = false)
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = { event = it },
            )
        }

        onNodeWithText(getString(Res.string.task_detail_mark_completed))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TaskDetailEvent.MarkCompletedClicked
    }

    @Test
    fun `should emit mark not completed event`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture(isComplete = true)
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = { event = it },
            )
        }

        onNodeWithText(getString(Res.string.task_detail_status_completed))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TaskDetailEvent.MarkNotCompletedClicked
    }

    @Test
    fun `should emit edit event`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture()
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TaskDetailScreen:MenuButton")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(getString(Res.string.task_detail_edit))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TaskDetailEvent.EditClicked
    }

    @Test
    fun `should show delete dialog and emit delete event`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture()
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TaskDetailScreen:MenuButton")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(getString(Res.string.task_detail_delete))
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(getString(Res.string.task_detail_delete_dialog_title))
            .assertIsDisplayed()

        onNodeWithText(getString(Res.string.task_detail_delete_dialog_confirm))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TaskDetailEvent.DeleteClicked
    }

    @Test
    fun `should dismiss delete dialog when cancel is clicked`() = runComposeUiTest {
        val task = TaskDetailViewData.Loaded.TaskViewData.fixture()
        var event: TaskDetailEvent? = null
        val message = getString(Res.string.task_detail_delete_dialog_message_1) +
            " \"${task.title}\" " +
            getString(Res.string.task_detail_delete_dialog_message_2)

        setContent {
            TaskDetailScreen(
                viewData = TaskDetailViewData.Loaded(task),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TaskDetailScreen:MenuButton")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(getString(Res.string.task_detail_delete))
            .assertIsDisplayed()
            .performClick()

        onNodeWithTag("TaskDetailScreen:DeleteDialogIcon")
            .assertIsDisplayed()

        onNodeWithTag("TaskDetailScreen:DeleteDialogMessage")
            .assertIsDisplayed()
            .assertTextEquals(message)

        onNodeWithTag("TaskDetailScreen:DeleteDialogCancelButton")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.general_cancel))
            .performClick()

        onNodeWithTag("TaskDetailScreen:DeleteDialog")
            .assertDoesNotExist()
        event shouldBe null
    }
}
