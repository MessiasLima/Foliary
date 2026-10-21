package dev.appoutlet.foliary.feature.signin

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.FoliaryKoinApplication
import io.kotest.matchers.shouldBe
import org.koin.core.context.stopKoin
import org.koin.plugin.module.dsl.startKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SignInScreenTest {

    @BeforeTest
    fun setup() {
        startKoin<FoliaryKoinApplication>()
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `should render sign in screen with card inset in scroll container`() = runComposeUiTest {
        setContent {
            SignInScreen()
        }

        onNodeWithTag("SignInScreen:ScrollContainer").assertIsDisplayed()
        onNodeWithTag("SignInScreen:Card").assertIsDisplayed()

        val scrollContainerBounds = onNodeWithTag("SignInScreen:ScrollContainer").getUnclippedBoundsInRoot()
        val cardBounds = onNodeWithTag("SignInScreen:Card").getUnclippedBoundsInRoot()

        val leftInset = cardBounds.left - scrollContainerBounds.left
        val rightInset = scrollContainerBounds.right - cardBounds.right

        (leftInset.value >= 24.dp.value) shouldBe true
        (rightInset.value >= 24.dp.value) shouldBe true
    }
}
