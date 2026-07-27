package dev.appoutlet.foliary.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.profile_logout
import foliary.foliary.generated.resources.profile_statistics
import foliary.foliary.generated.resources.profile_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(lazyListState: LazyListState = rememberLazyListState()) {
    val viewModel = koinViewModel<ProfileViewModel>()
    ProfileScreenContent(lazyListState, viewModel)
}

@Composable
internal fun ProfileScreenContent(
    lazyListState: LazyListState,
    viewModel: ProfileViewModel
) {
    Screen(
        screenName = "ProfileScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) {
        LazyColumn(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .fillMaxSize(),
            state = lazyListState,
        ) {
            item {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(Res.string.profile_title),
                    style = MaterialTheme.typography.displaySmall
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(Res.string.profile_statistics)) },
                    modifier = Modifier.clickable {
                        viewModel.onEvent(ProfileEvent.OnStatisticsClick)
                    }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text(text = stringResource(Res.string.profile_logout)) },
                    modifier = Modifier.clickable {
                        viewModel.onEvent(ProfileEvent.OnLogOutClick)
                    }
                )
            }
        }
    }
}

private fun onAction(action: ProfileAction, navigator: Navigator) {
    when (action) {
        ProfileAction.NavigateToSignIn -> navigator.setRoot(SignInNavKey)
    }
}
