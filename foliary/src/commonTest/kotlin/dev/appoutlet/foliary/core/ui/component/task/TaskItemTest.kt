package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_item_overdue
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TaskItemTest {

    @Test
    fun `should render title and description`() = runComposeUiTest {
        val title = "Task title"
        val description = "Task description"

        setContent {
            TaskItem(
                title = title,
                description = description,
            )
        }

        onNodeWithTag("TaskItem:Title").assertIsDisplayed().assertTextEquals(title)
        onNodeWithTag("TaskItem:Description").assertIsDisplayed().assertTextEquals(description)
    }

    @Test
    fun `should not show description when null`() = runComposeUiTest {
        setContent {
            TaskItem(title = "Task title")
        }

        onNodeWithTag("TaskItem:Description").assertDoesNotExist()
    }

    @Test
    fun `should show unchecked checkbox by default`() = runComposeUiTest {
        setContent {
            TaskItem(title = "Task title")
        }

        onNodeWithTag("TaskItem:Checkbox").assertIsDisplayed()
    }

    @Test
    fun `should show overdue pill when isOverdue is true and not completed`() = runComposeUiTest {
        setContent {
            TaskItem(
                title = "Task title",
                isOverdue = true,
                isCompleted = false,
            )
        }

        onNodeWithTag("TaskItem:OverduePill").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_item_overdue)).assertIsDisplayed()
    }

    @Test
    fun `should not show overdue pill when not overdue`() = runComposeUiTest {
        setContent {
            TaskItem(
                title = "Task title",
                isOverdue = false,
            )
        }

        onNodeWithTag("TaskItem:OverduePill").assertDoesNotExist()
    }

    @Test
    fun `should not show overdue pill when completed`() = runComposeUiTest {
        setContent {
            TaskItem(
                title = "Task title",
                isOverdue = true,
                isCompleted = true,
            )
        }

        onNodeWithTag("TaskItem:OverduePill").assertDoesNotExist()
    }

    @Test
    fun `should show checked checkbox and distinct title when completed`() = runComposeUiTest {
        setContent {
            TaskItem(
                title = "Task title",
                isCompleted = true,
            )
        }

        onNodeWithTag("TaskItem:Checkbox").assertIsDisplayed()
        onNodeWithTag("TaskItem:Title").assertIsDisplayed()
    }
}
