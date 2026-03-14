package dev.appoutlet.foliary.feature.signin

import androidx.lifecycle.viewModelScope
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ErrorState
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import io.github.jan.supabase.auth.event.AuthEvent
import io.github.jan.supabase.auth.status.RefreshFailureCause
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class SignInViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val deeplinkDispatcher: DeepLinkDispatcher
) : MviViewModel<SignInViewData, SignInAction>() {
    private var wasNotAuthenticated = false

    override val container = container(SignInViewData.Idle) {
        deeplinkDispatcher.deeplinks
            .onEach(::processDeeplink)
            .launchIn(viewModelScope)

        authenticationRepository.sessionStatus()
            .onEach(::handleSessionStatus)
            .launchIn(viewModelScope)
    }

    private fun handleSessionStatus(sessionStatus: SessionStatus) = intent {
        when (sessionStatus) {
            is SessionStatus.Authenticated -> {
                if (wasNotAuthenticated) {
                    reduce {
                        SignInViewData.Authenticated(
                            userName = sessionStatus.session.user?.email ?: "",
                            newUser = sessionStatus.isNew
                        )
                    }
                    delay(1.seconds)
                }
                postSideEffect(SignInAction.NavigateToMain)
            }

            is SessionStatus.NotAuthenticated -> {
                wasNotAuthenticated = true
                reduce { SignInViewData.UnAuthenticated() }
            }

            SessionStatus.Initializing -> reduce { SignInViewData.Loading }

            is SessionStatus.RefreshFailure -> {
                val errorState = when(val cause = sessionStatus.cause) {
                    is RefreshFailureCause.InternalServerError -> ErrorState(cause.exception)
                    is RefreshFailureCause.NetworkError -> ErrorState(cause.exception)
                }

                onError(errorState)
            }
        }
    }

    fun onTryAgain() = intent { reduce { SignInViewData.UnAuthenticated() } }

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
        reduce { SignInViewData.UnAuthenticated(requestingMagicLink = true) }
        authenticationRepository.requestMagicLink(email)
        reduce { SignInViewData.MagicLinkSent(email) }
    }
}

sealed interface SignInViewData{
    data object Idle : SignInViewData
    data class UnAuthenticated(val requestingMagicLink: Boolean = false) : SignInViewData
    data class MagicLinkSent(val email: String) : SignInViewData
    data object Loading : SignInViewData
    data class Authenticated(val userName: String, val newUser: Boolean) : SignInViewData
}

sealed interface SignInEvent {
    data object OnGoogleSignInClick : SignInEvent
    data object OnAppleSignInClick : SignInEvent
    data class OnSendMagicLink(val email: String) : SignInEvent
}

sealed interface SignInAction : Action {
    data object NavigateToMain : SignInAction
}
