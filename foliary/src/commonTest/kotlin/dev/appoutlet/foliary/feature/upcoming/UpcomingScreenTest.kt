package dev.appoutlet.foliary.feature.upcoming

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.upcoming_title
import org.jetbrains.compose.resources.getString
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UpcomingScreenTest {
    @Test
    fun `should render upcoming title`() = runComposeUiTest {
        setContent {
            UpcomingScreen(lazyListState = rememberLazyListState())
        }

        onNodeWithText(getString(Res.string.upcoming_title)).assertIsDisplayed()
    }
}
