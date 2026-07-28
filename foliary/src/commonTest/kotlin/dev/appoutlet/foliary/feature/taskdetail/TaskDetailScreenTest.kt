package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.back_icon_button_a11y
import foliary.foliary.generated.resources.task_detail_creation_date_label
import foliary.foliary.generated.resources.task_detail_delete
import foliary.foliary.generated.resources.task_detail_description_label
import foliary.foliary.generated.resources.task_detail_due_date_label
import foliary.foliary.generated.resources.task_detail_mark_completed
import foliary.foliary.generated.resources.task_detail_no_description
import foliary.foliary.generated.resources.task_detail_no_due_date
import foliary.foliary.generated.resources.task_detail_no_priority
import foliary.foliary.generated.resources.task_detail_priority_label
import foliary.foliary.generated.resources.task_detail_task_title_label
import foliary.foliary.generated.resources.task_detail_title
import foliary.foliary.generated.resources.task_detail_url_label
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test
import kotlin.time.Instant

@OptIn(ExperimentalTestApi::class)
class TaskDetailScreenTest {
    @Test
    fun `should render task detail fields`() = runComposeUiTest {
        val task = Task.fixture(
            title = "Task title",
            description = "Task description",
            dueDate = Instant.parse("2026-07-21T10:00:00Z"),
            priority = Priority.MEDIUM,
            completionDate = null,
            url = "https://foliary.appoutlet.dev/",
        )

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_detail_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_task_title_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:Title").assertIsDisplayed()
        onNodeWithText(task.title).assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_description_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:Description").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_due_date_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:DueDate").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_priority_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:Priority").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_url_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:Url").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_creation_date_label)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:CreationDate").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_mark_completed)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_delete)).assertIsDisplayed()
    }

    @Test
    fun `should hide url row when url is absent`() = runComposeUiTest {
        val task = Task.fixture(url = null)

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_detail_url_label)).assertDoesNotExist()
        onNodeWithTag("TaskDetailScreen:Url").assertDoesNotExist()
    }

    @Test
    fun `should hide url row when url is blank`() = runComposeUiTest {
        val task = Task.fixture(url = " ")

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_detail_url_label)).assertDoesNotExist()
        onNodeWithTag("TaskDetailScreen:Url").assertDoesNotExist()
    }

    @Test
    fun `should show overdue indicator when task is overdue`() = runComposeUiTest {
        val task = Task.fixture(
            dueDate = Instant.parse("2026-07-21T10:00:00Z"),
            completionDate = null,
        )

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = true),
                onEvent = {},
            )
        }

        onNodeWithTag("TaskDetailScreen:OverdueIndicator").assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:CompletedIndicator").assertDoesNotExist()
    }

    @Test
    fun `should show completed indicator when task is completed`() = runComposeUiTest {
        val task = Task.fixture(
            dueDate = Instant.parse("2026-07-21T10:00:00Z"),
            completionDate = Instant.parse("2026-07-21T11:00:00Z"),
        )

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithTag("TaskDetailScreen:CompletedIndicator").assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:OverdueIndicator").assertDoesNotExist()
    }

    @Test
    fun `should hide status indicator when task is unfinished and not overdue`() = runComposeUiTest {
        val task = Task.fixture(
            dueDate = null,
            completionDate = null,
        )

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithTag("TaskDetailScreen:OverdueIndicator").assertDoesNotExist()
        onNodeWithTag("TaskDetailScreen:CompletedIndicator").assertDoesNotExist()
    }

    @Test
    fun `should show fallback text for null fields`() = runComposeUiTest {
        val task = Task.fixture(
            description = null,
            dueDate = null,
            priority = null,
            completionDate = null,
            url = null,
        )

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_detail_no_description)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_no_due_date)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_detail_no_priority)).assertIsDisplayed()
        onNodeWithTag("TaskDetailScreen:CompletionDate").assertDoesNotExist()
    }

    @Test
    fun `should emit back clicked event`() = runComposeUiTest {
        val task = Task.fixture()
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = { event = it },
            )
        }

        onNodeWithContentDescription(getString(Res.string.back_icon_button_a11y))
            .performClick()

        event shouldBe TaskDetailEvent.BackClicked
    }

    @Test
    fun `should emit mark completed event`() = runComposeUiTest {
        val task = Task.fixture(completionDate = null)
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TaskDetailScreen:MarkCompletedButton").performClick()

        event shouldBe TaskDetailEvent.MarkCompletedClicked
    }

    @Test
    fun `should emit delete event`() = runComposeUiTest {
        val task = Task.fixture()
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(task = task, isOverdue = false),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TaskDetailScreen:DeleteButton").performClick()

        event shouldBe TaskDetailEvent.DeleteClicked
    }
}
