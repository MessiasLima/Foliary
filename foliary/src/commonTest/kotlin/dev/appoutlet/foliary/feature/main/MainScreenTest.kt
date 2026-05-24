package dev.appoutlet.foliary.feature.main

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import dev.appoutlet.foliary.FoliaryKoinApplication
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.main_nav_profile
import foliary.foliary.generated.resources.main_nav_today
import org.jetbrains.compose.resources.getString
import org.koin.core.context.stopKoin
import org.koin.plugin.module.dsl.startKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainScreenTest {
    @BeforeTest
    fun setup() {
        startKoin<FoliaryKoinApplication>()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should load tabs on today screen`() = runComposeUiTest {
        setContent {
            MainScreen()
        }

        // Tabs are shown
        onNodeWithText(getString(Res.string.main_nav_today))
            .assertIsDisplayed()
            .assertIsSelected()

        onNodeWithText(getString(Res.string.main_nav_profile))
            .assertIsDisplayed()
            .assertIsNotSelected()

        // Today is displayed
        onNodeWithTag("TodayScreen")
            .assertIsDisplayed()
    }

    @Test
    fun `should navigate to profile screen`() = runComposeUiTest {
        setContent {
            MainScreen()
        }

        onNodeWithText(getString(Res.string.main_nav_profile))
            .assertIsDisplayed()
            .performClick()

        onNodeWithTag("ProfileScreen")
            .assertIsDisplayed()
    }
}
