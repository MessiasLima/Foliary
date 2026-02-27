package dev.appoutlet.foliary.core.mvi

sealed interface State {
    object Idle : State
    data class Loading(val message: String? = null) : State
    data class Success<T : ViewData>(val data: T) : State
    data class Error(val throwable: Throwable? = null) : State
}