package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_item_overdue
import io.kotest.matchers.shouldBe
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
    fun `should toggle completion state when checkbox is clicked`() = runComposeUiTest {
        val completed = mutableStateOf(false)

        setContent {
            TaskItem(
                title = "Task title",
                isCompleted = completed.value,
                onCompletedChange = { completed.value = it },
            )
        }

        onNodeWithTag("TaskItem:Checkbox").assertIsOff()

        onNodeWithTag("TaskItem:Checkbox").performClick()
        onNodeWithTag("TaskItem:Checkbox").assertIsOn()

        onNodeWithTag("TaskItem:Checkbox").performClick()
        onNodeWithTag("TaskItem:Checkbox").assertIsOff()
    }

    @Test
    fun `should call onStartClick when start button is clicked`() = runComposeUiTest {
        var started = false

        setContent {
            TaskItem(
                title = "Task title",
                onStartClick = { started = true },
            )
        }

        onNodeWithTag("TaskItem:StartButton").performClick()

        started shouldBe true
    }
}
