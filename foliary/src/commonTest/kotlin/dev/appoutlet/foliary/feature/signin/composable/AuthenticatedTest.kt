package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.feature.signin.SignInViewData
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AuthenticatedTest {

    @Test
    fun `should show welcome message for new user`() = runComposeUiTest {
        val userName = "John Doe"
        val state = SignInViewData.Authenticated(userName = userName, newUser = true)

        setContent {
            Authenticated(state = state)
        }

        onNodeWithText("Welcome,").assertIsDisplayed()
        onNodeWithText("$userName!").assertIsDisplayed()
    }

    @Test
    fun `should show welcome back message for existing user`() = runComposeUiTest {
        val userName = "Jane Doe"
        val state = SignInViewData.Authenticated(userName = userName, newUser = false)

        setContent {
            Authenticated(state = state)
        }

        onNodeWithText("Welcome back,").assertIsDisplayed()
        onNodeWithText("$userName!").assertIsDisplayed()
    }
}
