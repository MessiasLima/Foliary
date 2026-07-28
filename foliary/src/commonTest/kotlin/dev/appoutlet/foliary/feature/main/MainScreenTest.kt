package dev.appoutlet.foliary.feature.main

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import dev.appoutlet.foliary.FoliaryKoinApplication
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.analytics.MockAnalytics
import org.koin.core.context.stopKoin
import org.koin.plugin.module.dsl.startKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {
    private val mockAnalytics = MockAnalytics()

    @BeforeTest
    fun setup() {
        startKoin<FoliaryKoinApplication>()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should render bottom navigation with three tabs`() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                MainScreen()
            }
        }

        waitUntilAtLeastOneExists(hasTestTag("TodayScreen"))

        onNodeWithTag("MainScreen:TodayTab").assertIsDisplayed()

        onNodeWithTag("MainScreen:UpcomingTab").assertIsDisplayed().performClick()
        waitUntilAtLeastOneExists(hasTestTag("UpcomingScreen"))

        onNodeWithTag("MainScreen:ProfileTab").assertIsDisplayed().performClick()
        waitUntilAtLeastOneExists(hasTestTag("ProfileScreen"))
    }
}
