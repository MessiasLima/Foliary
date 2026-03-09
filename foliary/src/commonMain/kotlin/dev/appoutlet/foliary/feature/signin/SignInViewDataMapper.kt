package dev.appoutlet.foliary.feature.signin

import org.koin.core.annotation.Factory

@Factory
class SignInViewDataMapper {
    operator fun invoke(
        email: String,
        isMagicLinkSent: Boolean,
        loading: Boolean,
    ) = SignInViewData(
        email = email,
        isMagicLinkSent = isMagicLinkSent,
        isLoading = loading,
    )
}