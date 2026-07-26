package dev.appoutlet.foliary.feature.main

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.test.waitUntilAtLeastOneExists
import dev.appoutlet.foliary.FoliaryKoinApplication
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.analytics.MockAnalytics
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.main_nav_profile
import foliary.foliary.generated.resources.main_nav_today
import foliary.foliary.generated.resources.main_nav_upcoming
import foliary.foliary.generated.resources.profile_title
import foliary.foliary.generated.resources.today_title
import foliary.foliary.generated.resources.upcoming_title
import org.jetbrains.compose.resources.getString
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

        onNodeWithText(getString(Res.string.main_nav_today)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.main_nav_upcoming)).assertIsDisplayed()
        onNodeWithText(getString(Res.string.main_nav_profile)).assertIsDisplayed()
    }

    @Test
    fun `should switch tabs and display corresponding screen`() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalAnalytics provides mockAnalytics) {
                MainScreen()
            }
        }

        waitUntilAtLeastOneExists(hasText(getString(Res.string.today_title)))

        onNodeWithText(getString(Res.string.main_nav_upcoming)).performClick()
        waitUntilAtLeastOneExists(hasText(getString(Res.string.upcoming_title)))

        onNodeWithText(getString(Res.string.main_nav_profile)).performClick()
        waitUntilAtLeastOneExists(hasText(getString(Res.string.profile_title)))
    }
}
