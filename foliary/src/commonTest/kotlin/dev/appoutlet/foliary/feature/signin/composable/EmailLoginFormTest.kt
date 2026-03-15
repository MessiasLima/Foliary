package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.feature.signin.SignInEvent
import dev.appoutlet.foliary.feature.signin.SignInViewData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class EmailLoginFormTest {

    @Test
    fun `should show initial state`() = runComposeUiTest {
        var event: SignInEvent? = null
        setContent {
            EmailLoginForm(
                viewData = SignInViewData.NotAuthenticated(requestingMagicLink = false),
                onEvent = { event = it }
            )
        }

        onNodeWithText("name@example.com").assertExists()
        onNodeWithText("Send Magic Link")
            .assertExists()
            .assertIsEnabled()
            .performClick()

        event.shouldBeInstanceOf<SignInEvent.OnSendMagicLink>()
        (event as SignInEvent.OnSendMagicLink).email shouldBe "test@example.com"
    }

    @Test
    fun `should show error on invalid email`() = runComposeUiTest {
        setContent {
            EmailLoginForm(
                viewData = SignInViewData.NotAuthenticated(requestingMagicLink = false),
                onEvent = {}
            )
        }

        onNodeWithText("name@example.com").performTextInput("invalid-email")
        onNodeWithText("Send Magic Link").performClick()

        // Wait for coroutine to finish and UI to update
        onNodeWithText("Please provide a valid e-mail.").assertExists()
    }

    @Test
    fun `should disable fields when loading`() = runComposeUiTest {
        setContent {
            EmailLoginForm(
                viewData = SignInViewData.NotAuthenticated(requestingMagicLink = true),
                onEvent = {}
            )
        }

        onNodeWithText("name@example.com").assertIsNotEnabled()
        onNodeWithText("Send Magic Link").assertIsNotEnabled()
    }
}
