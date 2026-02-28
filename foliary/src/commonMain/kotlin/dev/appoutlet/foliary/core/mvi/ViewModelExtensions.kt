package dev.appoutlet.foliary.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.SettingsBuilder
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax

fun <SideEffect : Action> ViewModel.container(
    initialState: State = State.Idle,
    buildSettings: SettingsBuilder.() -> Unit = {},
    onCreate: (suspend Syntax<State, SideEffect>.() -> Unit)? = null
): Container<State, SideEffect> {
    return viewModelScope.container(
        initialState = initialState,
        buildSettings = buildSettings,
        onCreate = onCreate
    )
}
