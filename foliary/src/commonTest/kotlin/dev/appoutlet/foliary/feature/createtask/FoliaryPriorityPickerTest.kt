package dev.appoutlet.foliary.feature.createtask

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.data.task.database.entity.Priority
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.create_task_priority_label
import foliary.foliary.generated.resources.priority_pill_blocker
import foliary.foliary.generated.resources.priority_pill_high
import foliary.foliary.generated.resources.priority_pill_highest
import foliary.foliary.generated.resources.priority_pill_low
import foliary.foliary.generated.resources.priority_pill_lowest
import foliary.foliary.generated.resources.priority_pill_medium
import io.kotest.matchers.shouldBe
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliaryPriorityPickerTest {

    @Test
    fun `should render selected priority as pill`() = runComposeUiTest {
        val label = getString(Res.string.create_task_priority_label)

        setContent {
            FoliaryPriorityPicker(
                selectedPriority = Priority.MEDIUM,
                onPrioritySelected = {},
                label = label
            )
        }

        onNodeWithText(label).assertIsDisplayed()
        onNodeWithText(getString(Res.string.priority_pill_medium)).assertIsDisplayed()
        onNodeWithTag("FoliaryPriorityPicker:Value", useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun `no menu items should exist before card is clicked`() = runComposeUiTest {
        setContent {
            FoliaryPriorityPicker(
                selectedPriority = Priority.MEDIUM,
                onPrioritySelected = {},
                label = stringResource(Res.string.create_task_priority_label)
            )
        }

        onNodeWithText(getString(Res.string.priority_pill_lowest)).assertDoesNotExist()
        onNodeWithText(getString(Res.string.priority_pill_high)).assertDoesNotExist()
        onNodeWithText(getString(Res.string.priority_pill_blocker)).assertDoesNotExist()
    }

    @Test
    fun `should open menu and list all six labels when card is clicked`() = runComposeUiTest {
        setContent {
            FoliaryPriorityPicker(
                selectedPriority = Priority.MEDIUM,
                onPrioritySelected = {},
                label = stringResource(Res.string.create_task_priority_label)
            )
        }

        onNodeWithTag("FoliaryPriorityPicker:Card", useUnmergedTree = true).performClick()
        waitForIdle()

        onNodeWithText(getString(Res.string.priority_pill_lowest)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.priority_pill_low)).assertIsDisplayed()
        onAllNodesWithText(getString(Res.string.priority_pill_medium)).assertCountEquals(2)
        onNodeWithText(getString(Res.string.priority_pill_high)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.priority_pill_highest)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.priority_pill_blocker)).assertIsDisplayed()
    }

    @Test
    fun `selecting an option should invoke callback and close menu`() = runComposeUiTest {
        var selectedPriority: Priority? = null

        setContent {
            FoliaryPriorityPicker(
                selectedPriority = Priority.MEDIUM,
                onPrioritySelected = { selectedPriority = it },
                label = stringResource(Res.string.create_task_priority_label)
            )
        }

        onNodeWithTag("FoliaryPriorityPicker:Card", useUnmergedTree = true).performClick()
        waitForIdle()

        onNodeWithText(getString(Res.string.priority_pill_high)).performClick()
        waitForIdle()

        selectedPriority shouldBe Priority.HIGH

        onNodeWithText(getString(Res.string.priority_pill_lowest)).assertDoesNotExist()
        onNodeWithText(getString(Res.string.priority_pill_blocker)).assertDoesNotExist()
    }
}
