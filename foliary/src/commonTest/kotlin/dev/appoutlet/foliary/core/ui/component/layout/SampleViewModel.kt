package dev.appoutlet.foliary.core.ui.component.layout

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ErrorState
import dev.appoutlet.foliary.core.mvi.MviViewModel

class SampleViewModel(initialState: SampleViewData) : MviViewModel<SampleViewData, SampleAction>() {
    override val container = container(initialState = initialState)

    fun emitAction(action: SampleAction) = intent {
        postSideEffect(action)
    }

    fun setError(error: ErrorState) {
        onError(error)
    }
}

data object SampleViewData
data object SampleAction : Action
