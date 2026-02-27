package dev.appoutlet.foliary.feature.signin

import androidx.lifecycle.ViewModel
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.State
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.mvi.container
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel : ViewModel(), ContainerHost<SignInAction> {
    override val container = container<SignInAction>(State.Success(SignInViewData))

    fun onEvent(event: SignInEvent) {
    }
}

data object SignInViewData : ViewData

sealed interface SignInEvent

sealed interface SignInAction : Action