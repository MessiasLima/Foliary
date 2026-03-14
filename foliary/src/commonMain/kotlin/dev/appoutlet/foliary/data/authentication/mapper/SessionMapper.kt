package dev.appoutlet.foliary.data.authentication.mapper

import dev.appoutlet.foliary.data.authentication.model.Session
import io.github.jan.supabase.auth.user.UserSession
import org.koin.core.annotation.Factory

@Factory
class SessionMapper {
    operator fun invoke(userSession: UserSession) = Session(
        accessToken = userSession.accessToken,
        refreshToken = userSession.refreshToken,
        providerRefreshToken = userSession.providerRefreshToken,
        providerToken = userSession.providerToken,
        expiresIn = userSession.expiresIn,
        tokenType = userSession.tokenType,
        type = userSession.type,
    )
}
