package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TodayViewModel : MviViewModel<TodayViewData, TodayAction>() {
    override val container = container(TodayViewData)

    fun onEvent(event: TodayEvent) {
        when (event) {
            TodayEvent.OnAddTaskClick -> onAddTaskClick()
        }
    }

    private fun onAddTaskClick() = intent {
        postSideEffect(TodayAction.NavigateToCreateTask)
    }
}

object TodayViewData

sealed interface TodayAction : Action {
    data object NavigateToCreateTask : TodayAction
}

sealed interface TodayEvent {
    data object OnAddTaskClick : TodayEvent
}
