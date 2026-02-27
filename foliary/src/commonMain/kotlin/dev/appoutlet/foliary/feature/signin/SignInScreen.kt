package dev.appoutlet.foliary.feature.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen() {
    val viewModel = koinViewModel<SignInViewModel>()
    Screen(
        viewModelProvider = { viewModel },
        onAction = ::onAction,
    ) { viewData: SignInViewData ->
        Scaffold { paddingValues ->
            Text(
                text = "Sign in",
                modifier = Modifier.padding(paddingValues),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

private fun onAction(action: SignInAction, navigator: Navigator) {}