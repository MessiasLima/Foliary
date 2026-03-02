package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.core.mvi.State
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ScreenTest {
    @Test
    fun `should start with idle state`() = runComposeUiTest {
        setContent {
            Screen(
                screenName = "TestScreen",
                viewModelProvider = { SampleViewModel(State.Idle) },
                idle = { Text("Idle screen") },
                content = { viewData: SampleViewData -> }
            )
        }

        onNodeWithText("Idle screen").assertExists()
    }
}
