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

    fun logOut() {
        intent {
            authenticationRepository.signOut()
        }
    }
}

object ProfileViewData
object ProfileAction : Action
