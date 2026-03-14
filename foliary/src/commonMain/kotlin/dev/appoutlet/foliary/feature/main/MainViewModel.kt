package dev.appoutlet.foliary.feature.main

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val authenticationRepository: AuthenticationRepository
) : MviViewModel<MainViewData, MainAction>() {
    override val container = container(initialState = MainViewData)

    fun logOut() {
        intent {
            authenticationRepository.signOut()
        }
    }
}

object MainViewData

object MainAction : Action