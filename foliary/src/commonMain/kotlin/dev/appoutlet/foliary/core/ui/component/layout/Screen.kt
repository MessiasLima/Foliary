package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.navigation.LocalNavigator
import dev.appoutlet.foliary.core.navigation.Navigator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import dev.appoutlet.foliary.core.mvi.State

@Suppress("UNCHECKED_CAST")
@Composable
fun <ScreenViewData : ViewData, SiteEffect : Action> Screen(
    viewModelProvider: @Composable () -> ContainerHost<SiteEffect>,
    modifier: Modifier = Modifier,
    onTryAgain: (() -> Unit)? = null,
    error: @Composable (Throwable?) -> Unit = { DefaultErrorIndicator(it?.message, onTryAgain) },
    loading: @Composable (String?) -> Unit = { DefaultLoadingIndicator(it) },
    idle: @Composable () -> Unit = {},
    onAction: suspend (SiteEffect, Navigator) -> Unit = { _, _ -> },
    content: @Composable (viewData: ScreenViewData) -> Unit,
) {
    val navigator = LocalNavigator.current
    val viewModel = viewModelProvider()
    val state by viewModel.collectAsState()

    viewModel.collectSideEffect(sideEffect = {
        onAction(it, navigator)
    })

    AnimatedContent(modifier = modifier, targetState = state) { state ->
        when (state) {
            is State.Error -> error(state.throwable)

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