package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.feature.signin.SignInEvent
import dev.appoutlet.foliary.feature.signin.SignInViewData
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalTestApi::class)
class MagicLinkSentTest {

    @Test
    fun `should show magic link sent content`() = runComposeUiTest {
        val email = "test@example.com"
        setContent {
            MagicLinkSent(
                state = SignInViewData.MagicLinkSent(email = email),
                onEvent = {}
            )
        }

        onNodeWithText("Magic link sent!").assertIsDisplayed()
        onNodeWithText("An email with a magic link was sent to: ").assertIsDisplayed()
        onNodeWithText(email).assertIsDisplayed()
        onNodeWithText("Check your inbox and your spam folder").assertIsDisplayed()
        // The expiration text starts at 300s
        onNodeWithText("The link will expire in", substring = true).assertIsDisplayed()

        onNodeWithText("Use another email").assertIsDisplayed().assertIsNotEnabled()
    }

    @Test
    fun `should enable button and trigger event after expiration`() = runComposeUiTest {
        var event: SignInEvent? = null
        val email = "test@example.com"

        setContent {
            MagicLinkSent(
                state = SignInViewData.MagicLinkSent(email = email),
                onEvent = { event = it }
            )
        }

        onNodeWithText("Use another email").assertIsNotEnabled()

        // Advance time by 300 seconds + some buffer
        mainClock.advanceTimeBy(305.seconds.inWholeMilliseconds)

        waitForIdle()

        onNodeWithText("Use another email").assertIsEnabled().performClick()

        event shouldBe SignInEvent.OnSelectNewEmail
    }
}
