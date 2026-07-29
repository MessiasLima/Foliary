package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class FoliaryTaskCardTest {

    @Test
    fun `should render a full filled card`() = runComposeUiTest {
        val fixture = FoliaryTaskCardViewData.fixture()

        setContent {
            FoliaryTaskCard(task = fixture)
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox")
            .assertIsDisplayed()
            .assertIsOff()

        onNodeWithTag("FoliaryTaskCard:Title")
            .assertIsDisplayed()
            .assertTextEquals(fixture.title)

        onNodeWithTag("FoliaryTaskCard:Description")
            .assertIsDisplayed()
            .assertTextEquals(fixture.description!!)

        onNodeWithTag("FoliaryTaskCard:OverduePill")
            .assertIsDisplayed()

        onNodeWithTag("FoliaryTaskCard:StartButton")
            .assertIsDisplayed()
    }

    @Test
    fun `should not show description when null`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(task = FoliaryTaskCardViewData.fixture(description = null))
        }

        onNodeWithTag("FoliaryTaskCard:Description").assertDoesNotExist()
    }

    @Test
    fun `should not show overdue pill when not overdue`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(task = FoliaryTaskCardViewData.fixture(isOverdue = false),)
        }

        onNodeWithTag("FoliaryTaskCard:OverduePill").assertDoesNotExist()
    }

    @Test
    fun `should not show overdue pill when completed`() = runComposeUiTest {
        setContent {
            FoliaryTaskCard(
                task = FoliaryTaskCardViewData.fixture(
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
                task = FoliaryTaskCardViewData.fixture(isCompleted = completed.value),
                onCompletedChange = { completed.value = it },
            )
        }

        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOff()

        onNodeWithTag("FoliaryTaskCard:Checkbox").performClick()
        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOn()

        onNodeWithTag("FoliaryTaskCard:Checkbox").performClick()
        onNodeWithTag("FoliaryTaskCard:Checkbox").assertIsOff()
    }
}
