package dev.appoutlet.foliary.core.mvi

import org.orbitmvi.orbit.ContainerHost as OrbitContainerHost

interface ContainerHost<SideEffect : Action, Event> : OrbitContainerHost<State, SideEffect> {
    fun onEvent(event: Event)
}