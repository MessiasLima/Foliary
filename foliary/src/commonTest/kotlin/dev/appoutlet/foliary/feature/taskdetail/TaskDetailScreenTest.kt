package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.back_icon_button_a11y
import foliary.foliary.generated.resources.task_detail_title
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class TaskDetailScreenTest {
    @Test
    fun `should render task detail title`() = runComposeUiTest {
        val task = Task.fixture()

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(
                    task = TaskDetailViewData.Loaded.TaskViewData(task.title),
                ),
                onEvent = {},
            )
        }

        onNodeWithText(getString(Res.string.task_detail_title)).assertIsDisplayed()
    }

    @Test
    fun `should emit back clicked event`() = runComposeUiTest {
        val task = Task.fixture()
        var event: TaskDetailEvent? = null

        setContent {
            TaskDetailScreen(
                taskId = task.id.toString(),
                viewData = TaskDetailViewData.Loaded(
                    task = TaskDetailViewData.Loaded.TaskViewData(task.title),
                ),
                onEvent = { event = it },
            )
        }

        onNodeWithContentDescription(getString(Res.string.back_icon_button_a11y))
            .performClick()

        event shouldBe TaskDetailEvent.BackClicked
    }
}
