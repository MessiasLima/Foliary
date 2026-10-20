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
import foliary.foliary.generated.resources.upcoming_group_header
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
                viewData = UpcomingViewState.Empty,
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
                viewData = UpcomingViewState.Empty,
                onEvent = { emittedEvent = it }
            )
        }

        onNodeWithContentDescription(getString(Res.string.upcoming_add_task_a11y))
            .assertIsDisplayed()
            .performClick()

        emittedEvent shouldBe UpcomingEvent.OnAddTaskClick
    }

    @Test
    fun `should render group headers and task items when viewData is Loaded`() = runComposeUiTest {
        val task = FoliaryTaskCardViewData.fixture(
            id = "task-1",
            title = "Test Task Title"
        )
        val group = UpcomingGroup.fixture(
            label = UpcomingGroupLabel.Overdue,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewState.Loaded(listOf(group)),
                onEvent = {}
            )
        }

        val overdueText = getString(Res.string.upcoming_group_overdue)
        val headerText = getString(Res.string.upcoming_group_header, overdueText, 1)

        onNodeWithText(headerText).assertIsDisplayed()
        onNodeWithText("Test Task Title").assertIsDisplayed()
    }

    @Test
    fun `should emit OnTaskClick event when task item is clicked`() = runComposeUiTest {
        var emittedEvent: UpcomingEvent? = null
        val task = FoliaryTaskCardViewData.fixture(
            id = "task-123",
            title = "Clickable Task"
        )
        val group = UpcomingGroup.fixture(
            label = UpcomingGroupLabel.Today,
            tasks = listOf(task)
        )

        setContent {
            UpcomingScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = UpcomingViewState.Loaded(listOf(group)),
                onEvent = { emittedEvent = it }
            )
        }

        onNodeWithText("Clickable Task").performClick()

        emittedEvent shouldBe UpcomingEvent.OnTaskClick("task-123")
    }
}
