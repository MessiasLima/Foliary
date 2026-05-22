package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class TodayViewModel : MviViewModel<TodayViewData, TodayAction>() {
    override val container = container(TodayViewData)
}

object TodayViewData
object TodayAction : Action
