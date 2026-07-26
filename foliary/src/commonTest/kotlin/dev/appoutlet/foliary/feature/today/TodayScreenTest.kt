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
                viewData = TodayViewData.Loaded(userName = userName, tasks = listOf(task)),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.today_title)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.today_welcome, userName)).assertIsDisplayed()
        onNodeWithTag("FoliaryTaskCard:Title").assertIsDisplayed()
        onNodeWithText(task.title).assertIsDisplayed()
    }

    @Test
    fun `should emit add task event from loaded content`() = runComposeUiTest {
        var event: TodayEvent? = null

        setContent {
            TodayScreenContent(
                lazyListState = rememberLazyListState(),
                viewData = TodayViewData.Loaded(
                    userName = "Messias",
                    tasks = listOf(FoliaryTaskCardViewData.fixture()),
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
            TodayScreenEmpty(
                viewData = TodayViewData.Empty(userName = userName),
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
            TodayScreenEmpty(
                viewData = TodayViewData.Empty(userName = "Messias"),
                onEvent = { event = it },
            )
        }

        onNodeWithText(getString(Res.string.today_empty_button))
            .assertIsDisplayed()
            .performClick()

        event shouldBe TodayEvent.OnAddTaskClick
    }
}
