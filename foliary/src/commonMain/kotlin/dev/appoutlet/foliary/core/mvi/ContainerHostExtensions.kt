package dev.appoutlet.foliary.core.mvi

fun <SideEffect : Action, Event> ContainerHost<SideEffect, Event>.emitState(
    showLoading: Boolean = true,
    block: suspend () -> State
) {
    intent {
        try {
            if (showLoading) reduce { State.Loading() }
            val newState = block()
            reduce { newState }
        } catch (throwable: Throwable) {
            reduce { State.Error(throwable) }
        }
    }
}

fun <SideEffect : Action, Event> ContainerHost<SideEffect, Event>.emitState(state: State) {
    intent {
        reduce { state }
    }
}

fun <SideEffect : Action, Event> ContainerHost<SideEffect, Event>.emitAction(
    showLoading: Boolean = true,
    block: suspend () -> SideEffect
) {
    intent {
        try {
            if (showLoading) reduce { State.Loading() }
            postSideEffect(block())
        } catch (throwable: Throwable) {
            reduce { State.Error(throwable) }
        }
    }
}