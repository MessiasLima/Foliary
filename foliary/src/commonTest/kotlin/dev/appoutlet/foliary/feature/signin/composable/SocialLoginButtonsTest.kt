package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SocialLoginButtonsTest {

    @Test
    fun shouldShowGoogleButtonWhenSupported() = runComposeUiTest {
        setContent {
            SocialLoginButtons(
                onEvent = {},
                isGoogleAuthSupported = true
            )
        }

        onNodeWithText("Continue with Google").assertExists()
    }

    @Test
    fun shouldNotShowGoogleButtonWhenNotSupported() = runComposeUiTest {
        setContent {
            SocialLoginButtons(
                onEvent = {},
                isGoogleAuthSupported = false
            )
        }

        onNodeWithText("Continue with Google").assertDoesNotExist()
    }
}
