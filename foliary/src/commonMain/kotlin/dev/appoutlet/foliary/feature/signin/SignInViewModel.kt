package dev.appoutlet.foliary.feature.signin

import androidx.lifecycle.ViewModel
import dev.appoutlet.foliary.core.analytics.Analytics
import dev.appoutlet.foliary.core.logging.logger
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ContainerHost
import dev.appoutlet.foliary.core.mvi.State
import dev.appoutlet.foliary.core.mvi.ViewData
import dev.appoutlet.foliary.core.mvi.container
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.providers.Apple
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.OTP
import org.koin.core.annotation.KoinViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

@KoinViewModel
class SignInViewModel(
    private val auth: Auth,
    private val analytics: Analytics,
) : ViewModel(), ContainerHost<SignInAction> {
    private val log by logger()
    override val container = container<SignInAction>(State.Success(SignInViewData()))

    fun onEvent(event: SignInEvent) {
        log.i { event.toString() }
        when (event) {
            SignInEvent.OnGoogleSignInClicked -> handleGoogleSignIn()
            SignInEvent.OnAppleSignInClicked -> handleAppleSignIn()
            is SignInEvent.OnEmailChanged -> handleEmailChanged(event.email)
            SignInEvent.OnSendMagicLink -> handleSendMagicLink()
        }
    }

    private fun handleGoogleSignIn() = intent {
        analytics.trackEvent("login_provider_selected", mapOf("provider" to "google"))
        reduce { State.Loading("Signing in with Google...") }
        runCatching {
            auth.signInWith(Google)
        }.onSuccess {
            reduce { State.Success(SignInViewData()) }
            postSideEffect(SignInAction.NavigateToMain)
        }.onFailure { throwable ->
            log.e(throwable) { "Google sign in failed" }
            reduce { State.Error(throwable) }
        }
    }

    private fun handleAppleSignIn() = intent {
        analytics.trackEvent("login_provider_selected", mapOf("provider" to "apple"))
        reduce { State.Loading("Signing in with Apple...") }
        runCatching {
            auth.signInWith(Apple)
        }.onSuccess {
            reduce { State.Success(SignInViewData()) }
            postSideEffect(SignInAction.NavigateToMain)
        }.onFailure { throwable ->
            log.e(throwable) { "Apple sign in failed" }
            reduce { State.Error(throwable) }
        }
    }

    private fun handleEmailChanged(email: String) = intent {
        val currentViewData = (state as? State.Success<SignInViewData>)?.data ?: SignInViewData()
        reduce { State.Success(currentViewData.copy(email = email)) }
    }

    private fun handleSendMagicLink() = intent {
        analytics.trackEvent("login_provider_selected", mapOf("provider" to "magic_link"))
        val currentViewData = (state as? State.Success<SignInViewData>)?.data ?: return@intent
        reduce { State.Loading("Sending magic link...") }
        runCatching {
            auth.signInWith(OTP) {
                email = currentViewData.email
            }
        }.onSuccess {
            reduce { State.Success(currentViewData.copy(isMagicLinkSent = true)) }
        }.onFailure { throwable ->
            log.e(throwable) { "Send magic link failed" }
            reduce { State.Error(throwable) }
        }
    }
}

data class SignInViewData(
    val email: String = "",
    val isMagicLinkSent: Boolean = false,
) : ViewData

sealed interface SignInEvent {
    data object OnGoogleSignInClicked : SignInEvent
    data object OnAppleSignInClicked : SignInEvent
    data class OnEmailChanged(val email: String) : SignInEvent
    data object OnSendMagicLink : SignInEvent
}

sealed interface SignInAction : Action {
    data object NavigateToMain : SignInAction
}
