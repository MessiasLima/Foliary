package dev.appoutlet.foliary.core.ui.component.layout

import androidx.lifecycle.ViewModel
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.State
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.mvi.container

class SampleViewModel(initialState: State) : ViewModel(), ContainerHost<SampleAction> {
    override val container = container<SampleAction>(initialState = initialState)
}

data object SampleViewData : ViewData
data object SampleAction : Action
