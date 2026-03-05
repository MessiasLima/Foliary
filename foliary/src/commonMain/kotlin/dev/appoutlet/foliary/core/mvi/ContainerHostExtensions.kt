package dev.appoutlet.foliary.core.mvi

fun <SideEffect : Action> ContainerHost<SideEffect>.emitState(
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

fun <SideEffect : Action> ContainerHost<SideEffect>.emitState(state: State) {
    intent {
        reduce { state }
    }
}

fun <SideEffect : Action> ContainerHost<SideEffect>.emitAction(
    showLoading: Boolean = true,
    block: suspend () -> SideEffect
) {
    intent {
        try {
            val previousState = state
            if (showLoading) reduce { State.Loading() }
            postSideEffect(block())
            reduce { previousState }
        } catch (throwable: Throwable) {
            reduce { State.Error(throwable) }
        }
    }
}
