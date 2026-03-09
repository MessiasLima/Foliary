package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
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
    val analytics = LocalAnalytics.current
    val viewModel = viewModelProvider()
    val state by viewModel.collectAsState()
    val log = remember { getLogger(screenName) }

    // Track screen view automatically
    LaunchedEffect(screenName) {
        analytics.trackScreen(screenName = screenName)
    }

    viewModel.collectSideEffect(sideEffect = {
        onAction(it, navigator)
    })

    val isError by derivedStateOf { state is State.Error }
    val isLoading by derivedStateOf { state is State.Loading }
    val isIdle by derivedStateOf { state is State.Idle }
    val isSuccess by derivedStateOf { state is State.Success<*> }

    AnimatedVisibility(visible = isIdle, enter = fadeIn(), exit = fadeOut()) {
        idle()
    }

    AnimatedVisibility(visible = isSuccess, enter = fadeIn(), exit = fadeOut()) {
        val successState = state as? State.Success<*>
        val viewData = remember(state) {
            successState?.data as? ScreenViewData ?: error("View data type mismatch")
        }
        content(viewData)
    }

    AnimatedVisibility(visible = isLoading, enter = fadeIn(), exit = fadeOut()) {
        val loadingState = state as? State.Loading
        loading(loadingState?.message)
    }

    AnimatedVisibility(visible = isError, enter = fadeIn(), exit = fadeOut()) {
        val errorState = state as? State.Error
        log.e(errorState?.throwable) { "Failure loading $screenName" }
        error(errorState?.throwable)
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
