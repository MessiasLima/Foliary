package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.data.task.database.entity.Priority
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.priority_pill_blocker
import foliary.foliary.generated.resources.priority_pill_high
import foliary.foliary.generated.resources.priority_pill_highest
import foliary.foliary.generated.resources.priority_pill_low
import foliary.foliary.generated.resources.priority_pill_lowest
import foliary.foliary.generated.resources.priority_pill_medium
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class PriorityPillTest {

    @Test
    fun `should expose a default test tag`() = runComposeUiTest {
        setContent {
            PriorityPill(priority = Priority.MEDIUM)
        }

        onNodeWithTag("PriorityPill")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_medium))
    }

    @Test
    fun `should render the correct text for each priority`() = runComposeUiTest {
        setContent {
            Column {
                PriorityPill(
                    priority = Priority.LOWEST,
                    modifier = Modifier.testTag("PriorityPill:Lowest")
                )
                PriorityPill(
                    priority = Priority.LOW,
                    modifier = Modifier.testTag("PriorityPill:Low")
                )
                PriorityPill(
                    priority = Priority.MEDIUM,
                    modifier = Modifier.testTag("PriorityPill:Medium")
                )
                PriorityPill(
                    priority = Priority.HIGH,
                    modifier = Modifier.testTag("PriorityPill:High")
                )
                PriorityPill(
                    priority = Priority.HIGHEST,
                    modifier = Modifier.testTag("PriorityPill:Highest")
                )
                PriorityPill(
                    priority = Priority.BLOCKER,
                    modifier = Modifier.testTag("PriorityPill:Blocker")
                )
            }
        }

        onNodeWithTag("PriorityPill:Lowest")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_lowest))

        onNodeWithTag("PriorityPill:Low")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_low))

        onNodeWithTag("PriorityPill:Medium")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_medium))

        onNodeWithTag("PriorityPill:High")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_high))

        onNodeWithTag("PriorityPill:Highest")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_highest))

        onNodeWithTag("PriorityPill:Blocker")
            .assertIsDisplayed()
            .assertTextEquals(getString(Res.string.priority_pill_blocker))
    }
}
