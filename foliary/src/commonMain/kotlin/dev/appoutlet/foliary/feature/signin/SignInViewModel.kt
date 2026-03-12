package dev.appoutlet.foliary.feature.signin

import androidx.lifecycle.viewModelScope
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val deeplinkDispatcher: DeepLinkDispatcher
) : MviViewModel<SignInViewData, SignInAction>() {
    override val container = container(SignInViewData()) {
        deeplinkDispatcher.deeplinks
            .onEach(::processDeeplink)
            .launchIn(viewModelScope)

        authenticationRepository.sessionStatus()
            .onEach(::handleSessionStatus)
            .launchIn(viewModelScope)
    }

    private fun handleSessionStatus(sessionStatus: SessionStatus) {

    }

    fun onTryAgain() = intent { reduce { SignInViewData() } }

    private fun processDeeplink(deepLink: Deeplink) = intent {
        val accessToken = deepLink.queryParameters["access_token"] ?: return@intent
        val refreshToken = deepLink.queryParameters["refresh_token"] ?: return@intent

        authenticationRepository.importAuthToken(accessToken, refreshToken)
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
