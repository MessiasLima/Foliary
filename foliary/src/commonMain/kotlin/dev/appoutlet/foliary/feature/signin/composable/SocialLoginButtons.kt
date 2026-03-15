package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.auth.isGoogleAuthSupported
import dev.appoutlet.foliary.core.ui.component.button.FoliaryOutlinedButton
import dev.appoutlet.foliary.feature.signin.SignInEvent
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.ic_google
import foliary.foliary.generated.resources.sign_in_continue_with_google
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SocialLoginButtons(
    onEvent: (SignInEvent) -> Unit,
    isGoogleAuthSupported: Boolean = isGoogleAuthSupported()
) {
    if (isGoogleAuthSupported) {
        FoliaryOutlinedButton(
            onClick = { onEvent(SignInEvent.OnGoogleSignInClick) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_google),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(Res.string.sign_in_continue_with_google),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
