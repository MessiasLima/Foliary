package dev.appoutlet.foliary.feature.profile

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository
) : MviViewModel<ProfileViewData, ProfileAction>() {
    override val container = container(ProfileViewData)

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnLogOutClick -> onLogOutClick()
        }
    }

    private fun onLogOutClick() = intent {
        authenticationRepository.signOut()
        postSideEffect(ProfileAction.NavigateToSignIn)
    }
}

object ProfileViewData
sealed interface ProfileAction : Action {
    data object NavigateToSignIn : ProfileAction
}

sealed interface ProfileEvent {
    data object OnLogOutClick : ProfileEvent
}
