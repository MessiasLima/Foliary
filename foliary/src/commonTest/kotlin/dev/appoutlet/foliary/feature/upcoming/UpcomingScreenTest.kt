package dev.appoutlet.foliary.feature.upcoming

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
import foliary.foliary.generated.resources.upcoming_add_task_a11y
import foliary.foliary.generated.resources.upcoming_description
import foliary.foliary.generated.resources.upcoming_empty_description
import foliary.foliary.generated.resources.upcoming_empty_title
import foliary.foliary.generated.resources.upcoming_group_overdue
import foliary.foliary.generated.resources.upcoming_title
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UpcomingScreenTest {

    @Test
    fun `should render title and empty state when viewData is Empty`() = runComposeUiTest {
        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Empty,
                onEvent = {}
            )
        }

        onNodeWithText(getString(Res.string.upcoming_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.upcoming_description)).assertIsDisplayed()
        onNodeWithTag("UpcomingScreen:Empty").assertIsDisplayed()
        onNodeWithText(getString(Res.string.upcoming_empty_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.upcoming_empty_description)).assertIsDisplayed()
    }

    @Test
    fun `should emit add task event when add button is clicked`() = runComposeUiTest {
        var emittedEvent: UpcomingEvent? = null

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Empty,
                onEvent = { emittedEvent = it }
            )
        }

        onNodeWithContentDescription(getString(Res.string.upcoming_add_task_a11y))
            .assertIsDisplayed()
            .performClick()

        emittedEvent shouldBe UpcomingEvent.OnAddTaskClick
    }

    @Test
    fun `should render group header and task items when viewData is Loaded`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(
            id = "task-1",
            title = "Test Task Title",
            isOverdue = false,
        )
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Overdue,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = {}
            )
        }

        onNodeWithTag("UpcomingScreen:GroupHeader").assertIsDisplayed()
        onNodeWithText(getString(Res.string.upcoming_group_overdue)).assertIsDisplayed()
        onNodeWithTag("UpcomingScreen:TaskItem").assertIsDisplayed()
        onNodeWithText("Test Task Title").assertIsDisplayed()
    }

    @Test
    fun `should render date group label when viewData is Loaded`() = runComposeUiTest {
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Date("07 Jun 2026"),
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = {}
            )
        }

        onNodeWithText("07 Jun 2026").assertIsDisplayed()
    }

    @Test
    fun `should emit OnTaskClick event when task item is clicked`() = runComposeUiTest {
        var emittedEvent: UpcomingEvent? = null
        val task = FoliaryTaskCardViewData.fixture(
            id = "task-123",
            title = "Clickable Task"
        )
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Today,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = { emittedEvent = it }
            )
        }

        onNodeWithText("Clickable Task").performClick()

        emittedEvent shouldBe UpcomingEvent.OnTaskClick("task-123")
    }

    @Test
    fun `should emit url pill click event when url pill is tapped`() = runComposeUiTest {
        var event: UpcomingEvent? = null
        val task = FoliaryTaskCardViewData.fixture(id = "task-url")
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Today,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:UrlPill").performClick()

        event shouldBe UpcomingEvent.OnUrlPillClick(task.id)
    }

    @Test
    fun `should emit MarkTaskAsCompleted when an unfinished task checkbox is checked`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(id = "task-id", isCompleted = false)
        var event: UpcomingEvent? = null
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Today,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        event shouldBe UpcomingEvent.MarkTaskAsCompleted(task.id)
    }

    @Test
    fun `should emit MarkTaskAsNotCompleted when a completed task checkbox is unchecked`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(id = "task-id", isCompleted = true)
        var event: UpcomingEvent? = null
        val group = UpcomingViewData.TaskGroup.fixture(
            label = UpcomingViewData.GroupLabel.Today,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewData.Loaded(listOf(group)),
                onEvent = { event = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox", useUnmergedTree = true)
            .assertIsDisplayed()
            .performClick()

        event shouldBe UpcomingEvent.MarkTaskAsNotCompleted(task.id)
    }
}
