package dev.appoutlet.foliary.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.appoutlet.foliary.core.logging.logger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.SettingsBuilder
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

abstract class MviViewModel<State: Any, SideEffect : Action> : ViewModel(), ContainerHost<State, SideEffect> {

    protected val log by logger()

    val errorState: StateFlow<ErrorState?>
        field = MutableStateFlow<ErrorState?>(null)

    protected fun container(
        initialState: State,
        buildSettings: SettingsBuilder.() -> Unit = {},
        onCreate: (suspend Syntax<State, SideEffect>.() -> Unit)? = null
    ) = viewModelScope.container(
        initialState = initialState,
        buildSettings = {
            exceptionHandler = CoroutineExceptionHandler { _, throwable ->
                log.e(throwable) { "Unhandled exception in MVI container" }
                errorState.update { ErrorState(throwable) }
            }
            buildSettings()
        },
        onCreate = onCreate
    )

    fun dismissError() {
        errorState.update { null }
    }
}
