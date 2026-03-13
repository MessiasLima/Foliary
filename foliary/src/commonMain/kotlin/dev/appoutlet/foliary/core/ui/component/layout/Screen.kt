package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.navigation.LocalNavigator
import dev.appoutlet.foliary.core.navigation.Navigator
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.error_default_message
import foliary.foliary.generated.resources.error_default_title
import org.jetbrains.compose.resources.stringResource
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Suppress("UNCHECKED_CAST")
@Composable
fun <State : Any, SideEffect : Action> Screen(
    screenName: String,
    viewModelProvider: @Composable () -> MviViewModel<State, SideEffect>,
    onTryAgain: (() -> Unit)? = null,
    onAction: suspend (SideEffect, Navigator) -> Unit = { _, _ -> },
    content: @Composable (viewData: State) -> Unit,
) {
    val navigator = LocalNavigator.current
    val analytics = LocalAnalytics.current
    val viewModel = viewModelProvider()
    val state by viewModel.collectAsState()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()

    LaunchedEffect(screenName) {
        analytics.trackScreen(screenName = screenName)
    }

    viewModel.collectSideEffect(sideEffect = {
        onAction(it, navigator)
    })

    AnimatedContent(errorState != null) { hasError ->
        if (hasError) {
            viewModel.errorState.value?.let { (throwable, title, message) ->
                ErrorIndicator(
                    modifier = Modifier.fillMaxSize(),
                    title = title ?: stringResource(Res.string.error_default_title),
                    message = message ?: stringResource(Res.string.error_default_message),
                    stackTrace = throwable.message,
                    onTryAgain = {
                        viewModel.dismissError()
                        onTryAgain?.invoke()
                    }
                )
            }
        } else {
            content(state)
        }
    }
}
