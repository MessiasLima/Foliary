package dev.appoutlet.foliary.feature.today

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.fixture
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_add_task_a11y
import foliary.foliary.generated.resources.today_celebration_button
import foliary.foliary.generated.resources.today_celebration_description
import foliary.foliary.generated.resources.today_celebration_title
import foliary.foliary.generated.resources.today_completed_header
import foliary.foliary.generated.resources.today_empty_button
import foliary.foliary.generated.resources.today_empty_description
import foliary.foliary.generated.resources.today_empty_title
import foliary.foliary.generated.resources.today_title
import foliary.foliary.generated.resources.today_welcome
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TodayScreenTest {
    @Test
    fun `should render loaded content`() = runComposeUiTest {
        val userName = "Messias"
        val task = FoliaryTaskCardViewData.fixture()

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = userName,
                    pendingTasks = listOf(task),
                    completedTasks = emptyList()
                ),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.today_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_welcome, userName)).assertIsDisplayed()
        onNodeWithText(task.title).assertIsDisplayed()
    }

    @Test
    fun `should emit task click event when task row is tapped`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(id = "task-id")
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(task),
                    completedTasks = emptyList()
                ),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TodayScreen:TaskItem")
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.OnTaskClick(task.id)
    }

    @Test
    fun `should emit add task event from loaded content`() = runComposeUiTest {
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(FoliaryTaskCardViewData.fixture()),
                    completedTasks = emptyList()
                ),
                onEvent = { event = it },
            )
        }

        onNodeWithContentDescription(getString(Res.string.today_add_task_a11y))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.OnAddTaskClick
    }

    @Test
    fun `should render empty content`() = runComposeUiTest {
        val userName = "Messias"

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Empty(userName = userName, completedTasks = emptyList()),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.today_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_welcome, userName)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_empty_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_empty_description)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_empty_button)).assertIsDisplayed()
    }

    @Test
    fun `should emit add task event from empty content`() = runComposeUiTest {
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Empty(userName = "Messias", completedTasks = emptyList()),
                onEvent = { event = it },
            )
        }

        onNodeWithText(getString(Res.string.today_empty_button))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.OnAddTaskClick
    }

    @Test
    fun `should render pending tasks above completed section`() = runComposeUiTest {
        val pendingTask = FoliaryTaskCardViewData.fixture(
            id = "pending-task",
            title = "Pending task",
            isCompleted = false
        )
        val completedTask = FoliaryTaskCardViewData.fixture(
            id = "completed-task",
            title = "Completed task",
            isCompleted = true
        )

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingTask),
                    completedTasks = listOf(completedTask)
                ),
                onEvent = {},
            )
        }

        onNodeWithText(pendingTask.title).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_completed_header, 1)).assertIsDisplayed()

        onNodeWithTag("TodayScreen:CompletedHeader").performClick()
        onNodeWithText(completedTask.title).assertIsDisplayed()
    }

    @Test
    fun `should show completed tasks when header is tapped`() = runComposeUiTest {
        val completedTask = FoliaryTaskCardViewData.fixture(
            id = "completed-task",
            title = "Completed task",
            isCompleted = true
        )

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = emptyList(),
                    completedTasks = listOf(completedTask)
                ),
                onEvent = {},
            )
        }

        onNodeWithText(completedTask.title).assertDoesNotExist()

        onNodeWithTag("TodayScreen:CompletedHeader")
            .assertIsDisplayed()
            .performClick()

        onNodeWithText(completedTask.title).assertIsDisplayed()
    }

    @Test
    fun `should hide completed tasks when header is tapped again`() = runComposeUiTest {
        val completedTask = FoliaryTaskCardViewData.fixture(
            id = "completed-task",
            title = "Completed task",
            isCompleted = true
        )

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = emptyList(),
                    completedTasks = listOf(completedTask)
                ),
                onEvent = {},
            )
        }

        onNodeWithTag("TodayScreen:CompletedHeader").performClick()
        onNodeWithText(completedTask.title).assertIsDisplayed()

        onNodeWithTag("TodayScreen:CompletedHeader").performClick()
        onNodeWithText(completedTask.title).assertDoesNotExist()
    }

    @Test
    fun `should render celebration content`() = runComposeUiTest {
        val completedTask = FoliaryTaskCardViewData.fixture(id = "completed-task", isCompleted = true)

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Celebration(
                    userName = "Messias",
                    completedTasks = listOf(completedTask)
                ),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.today_celebration_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_celebration_description)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_celebration_button)).assertIsDisplayed()

        onNodeWithTag("TodayScreen:CompletedHeader").performClick()
        onNodeWithText(completedTask.title).assertIsDisplayed()
    }

    @Test
    fun `should emit MarkTaskAsCompleted when an unfinished task checkbox is checked`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(id = "task-id", isCompleted = false)
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(task),
                    completedTasks = emptyList()
                ),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.MarkTaskAsCompleted(task.id)
    }

    @Test
    fun `should emit MarkTaskAsNotCompleted when a completed task checkbox is unchecked`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(id = "task-id", isCompleted = true)
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = emptyList(),
                    completedTasks = listOf(task)
                ),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("TodayScreen:CompletedHeader").performClick()

        onNodeWithTag("FoliaryTaskCard:Checkbox", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.MarkTaskAsNotCompleted(task.id)
    }
}
