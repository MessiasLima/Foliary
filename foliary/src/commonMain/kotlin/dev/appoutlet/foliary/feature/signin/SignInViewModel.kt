package dev.appoutlet.foliary.feature.signin

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository,
) : MviViewModel<SignInViewData, SignInAction>() {
    override val container = container(SignInViewData())

    fun onTryAgain() = intent { reduce { SignInViewData() } }

    fun onEvent(event: SignInEvent) {
        log.d { event.toString() }

        when (event) {
            SignInEvent.OnGoogleSignInClick -> handleGoogleSignIn()
            SignInEvent.OnAppleSignInClick -> handleAppleSignIn()
            is SignInEvent.OnSendMagicLink -> handleSendMagicLink(event.email)
        }
    }

    private fun handleGoogleSignIn() = intent {
    }

    private fun handleAppleSignIn() = intent {
    }

    private fun handleSendMagicLink(email: String) = intent {
        reduce { state.copy(isLoading = true) }
        authenticationRepository.requestMagicLink(email)
        reduce { state.copy(isLoading = false, isMagicLinkSent = true) }
    }

}

data class SignInViewData(
    val isMagicLinkSent: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface SignInEvent {
    data object OnGoogleSignInClick : SignInEvent
    data object OnAppleSignInClick : SignInEvent
    data class OnSendMagicLink(val email: String) : SignInEvent
}

sealed interface SignInAction : Action {
    data object NavigateToMain : SignInAction
}
