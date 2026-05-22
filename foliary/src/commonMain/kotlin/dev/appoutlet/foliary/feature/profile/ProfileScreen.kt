package dev.appoutlet.foliary.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.profile_logout
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    Screen(
        screenName = "ProfileScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FoliaryPrimaryButton(
                    onClick = { viewModel.onEvent(ProfileEvent.OnLogOutClick) }
                ) {
                    Text(text = stringResource(Res.string.profile_logout))
                }
            }
        }
    }
}


private fun onAction(action: ProfileAction, navigator: Navigator) {
    when(action) {
        ProfileAction.NavigateToSignIn -> navigator.setRoot(SignInNavKey)
    }
}
