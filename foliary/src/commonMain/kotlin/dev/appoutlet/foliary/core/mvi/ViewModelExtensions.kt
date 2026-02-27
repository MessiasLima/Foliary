package dev.appoutlet.foliary.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.SettingsBuilder
import org.orbitmvi.orbit.container
import org.orbitmvi.orbit.syntax.Syntax

fun <SIDE_EFFECT : Any> ViewModel.container(
    initialState: State = State.Idle,
    buildSettings: SettingsBuilder.() -> Unit = {},
    onCreate: (suspend Syntax<State, SIDE_EFFECT>.() -> Unit)? = null
): Container<State, SIDE_EFFECT> {
    return viewModelScope.container(
        initialState = initialState,
        buildSettings = buildSettings,
        onCreate = onCreate
    )
}
