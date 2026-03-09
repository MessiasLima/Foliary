package dev.appoutlet.foliary.feature.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.appoutlet.foliary.core.logging.logger
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.State
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.mvi.container
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val signInViewDataMapper: SignInViewDataMapper,
) : ViewModel(), ContainerHost<SignInAction> {
    private val log by logger()

    private val email = MutableStateFlow("")
    private val loading = MutableStateFlow(false)
    private val isMagicLinkSent = MutableStateFlow(false)

    override val container = container<SignInAction> {
        combine(email, loading, isMagicLinkSent) { email, loading, isMagicLinkSent ->
            signInViewDataMapper(
                email = email,
                isMagicLinkSent = isMagicLinkSent,
                loading = loading,
            )
        }
            .onEach { reduce { State.Success(it) } }
            .launchIn(viewModelScope)
    }

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

    private fun handleEmailChanged(email: String) {
        this.email.value = email
    }

    private fun handleSendMagicLink(email: String) = intent {

    }
}

data class SignInViewData(
    val email: String,
    val isMagicLinkSent: Boolean,
    val isLoading: Boolean,
) : ViewData

sealed interface SignInEvent {
    data object OnGoogleSignInClick : SignInEvent
    data object OnAppleSignInClick : SignInEvent
    data class OnSendMagicLink(val email: String) : SignInEvent
}

sealed interface SignInAction : Action {
    data object NavigateToMain : SignInAction
}
