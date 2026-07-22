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
class FoliaryTaskCardTest {

    @Test
    fun `should render title and description`() = runComposeUiTest {
        val title = "Task title"
        val description = "Task description"

        setContent {
            FoliaryTaskCard(
                task = taskViewData(
                    title = title,
                    description = description,
                ),
            )
        }

        onNodeWithTag("FoliaryTaskCard:Title").assertIsDisplayed().assertTextEquals(title)
        onNodeWithTag("FoliaryTaskCard:Description").assertIsDisplayed().assertTextEquals(description)
    }

    @Test
    fun `should not show description when null`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(task = taskViewData())
        }

        onNodeWithTag("FoliaryTaskCard:Description").assertDoesNotExist()
    }

    @Test
    fun `should show unchecked checkbox by default`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(task = taskViewData())
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsDisplayed()
    }

    @Test
    fun `should show overdue pill when isOverdue is true and not completed`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(
                task = taskViewData(
                    isOverdue = true,
                    isCompleted = false,
                ),
            )
        }

        onNodeWithTag("FoliaryTaskCard:OverduePill").assertIsDisplayed()
        onNodeWithText(getString(Res.string.task_item_overdue)).assertIsDisplayed()
    }

    @Test
    fun `should not show overdue pill when not overdue`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(
                task = taskViewData(isOverdue = false),
            )
        }

        onNodeWithTag("FoliaryTaskCard:OverduePill").assertDoesNotExist()
    }

    @Test
    fun `should not show overdue pill when completed`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(
                task = taskViewData(
                    isOverdue = true,
                    isCompleted = true,
                ),
            )
        }

        onNodeWithTag("FoliaryTaskCard:OverduePill").assertDoesNotExist()
    }

    @Test
    fun `should toggle completion state when checkbox is clicked`() = runComposeUiTest {
        val completed = mutableStateOf(false)

        setContent {
            FoliaryTaskCard(
                task = taskViewData(isCompleted = completed.value),
                onCompletedChange = { completed.value = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOff()

        onNodeWithTag("FoliaryTaskCard:Checkbox").performClick()
        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOn()

        onNodeWithTag("FoliaryTaskCard:Checkbox").performClick()
        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOff()
    }

    @Test
    fun `should call onStartClick when start button is clicked`() = runComposeUiTest {
        var started = false

        setContent {
            FoliaryTaskCard(
                task = taskViewData(),
                onStartClick = { started = true },
            )
        }

        onNodeWithTag("FoliaryTaskCard:StartButton").performClick()

        started shouldBe true
    }

    private fun taskViewData(
        id: String = "task-id",
        title: String = "Task title",
        description: String? = null,
        isCompleted: Boolean = false,
        isOverdue: Boolean = false,
    ) = FoliaryTaskCardViewData(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted,
        isOverdue = isOverdue,
    )
}
