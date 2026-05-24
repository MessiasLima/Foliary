package dev.appoutlet.foliary

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import org.koin.core.context.stopKoin
import org.koin.plugin.module.dsl.startKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppTest {
    @BeforeTest
    fun setup() {
        startKoin<FoliaryKoinApplication>()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `the application should start`() = runComposeUiTest {
        setContent {
            App()
        }

        onNodeWithTag("SignInScreen").assertIsDisplayed()
    }
}
