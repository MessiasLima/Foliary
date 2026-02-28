package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.State
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.navigation.LocalNavigator
import dev.appoutlet.foliary.core.navigation.Navigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Suppress("UNCHECKED_CAST")
@Composable
fun <ScreenViewData : ViewData, SideEffect : Action> Screen(
    screenName: String,
    viewModelProvider: @Composable () -> ContainerHost<SideEffect>,
    modifier: Modifier = Modifier,
    onTryAgain: (() -> Unit)? = null,
    error: @Composable (Throwable?) -> Unit = { DefaultErrorIndicator(it?.message, onTryAgain) },
    loading: @Composable (String?) -> Unit = { DefaultLoadingIndicator(it) },
    idle: @Composable () -> Unit = {},
    onAction: suspend (SideEffect, Navigator) -> Unit = { _, _ -> },
    content: @Composable (viewData: ScreenViewData) -> Unit,
) {
    val navigator = LocalNavigator.current
    val viewModel = viewModelProvider()
    val state by viewModel.collectAsState()
    val log = remember { getLogger(screenName) }

    viewModel.collectSideEffect(sideEffect = {
        onAction(it, navigator)
    })

    AnimatedContent(modifier = modifier.testTag(screenName), targetState = state) { state ->
        when (state) {
            is State.Error -> {
                log.e(state.throwable) {
                    state.throwable?.message ?: "A error occurred in $screenName"
                }

                error(state.throwable)
            }

            is State.Loading -> loading(state.message)

            is State.Success<*> -> {
                val viewData = remember(state) {
                    state.data as? ScreenViewData ?: error("View data type mismatch")
                }
                content(viewData)
            }

            State.Idle -> idle()
        }
    }
}

@Composable
private fun DefaultErrorIndicator(
    errorMessage: String?,
    onTryAgain: (() -> Unit)?,
) {
    ErrorIndicator(
        modifier = Modifier.fillMaxSize(),
        stackTrace = errorMessage,
        onTryAgain = onTryAgain
    )
}

@Composable
private fun DefaultLoadingIndicator(
    message: String? = null
) {
    LoadingIndicator(modifier = Modifier.fillMaxSize(), message = message)
}
