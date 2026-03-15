package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import dev.appoutlet.foliary.feature.signin.SocialLoginButtons
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SocialLoginButtonsTest {

    @Test
    fun shouldShowGoogleButton() = runComposeUiTest {
        setContent {
            SocialLoginButtons(
                onEvent = {}
            )
        }

        onNodeWithText("Continue with Google").assertExists().assertIsEnabled()
    }

    @Test
    fun shouldDisableButtonWhenRequesting() = runComposeUiTest {
         setContent {
            SocialLoginButtons(
                onEvent = {},
                requestingGoogleAuthentication = true
            )
        }

        onNodeWithText("Continue with Google").assertExists().assertIsNotEnabled()
    }
}
