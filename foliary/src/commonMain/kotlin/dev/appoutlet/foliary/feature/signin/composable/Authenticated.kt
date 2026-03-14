package dev.appoutlet.foliary.feature.signin.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.feature.signin.SignInViewData
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.sign_in_username
import foliary.foliary.generated.resources.sign_in_welcome
import foliary.foliary.generated.resources.sign_in_welcome_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun Authenticated(
    state: SignInViewData.Authenticated,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val welcomeText = if (state.newUser) {
            Res.string.sign_in_welcome
        } else {
            Res.string.sign_in_welcome_back
        }

        Text(stringResource(welcomeText), style = MaterialTheme.typography.titleMedium)

        Text(
            stringResource(Res.string.sign_in_username, state.userName),
            style = MaterialTheme.typography.titleLarge
        )
    }
}
