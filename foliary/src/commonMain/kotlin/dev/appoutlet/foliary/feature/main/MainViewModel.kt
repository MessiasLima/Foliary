package dev.appoutlet.foliary.feature.main

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MainViewModel : MviViewModel<MainViewData, MainAction>() {
    override val container = container(initialState = MainViewData())

    fun onTabSelected(tab: MainTab) = intent {
        reduce { state.copy(selectedTab = tab) }
    }
}

data class MainViewData(
    val selectedTab: MainTab = MainTab.Today
)

enum class MainTab {
    Today, Profile
}

object MainAction : Action
