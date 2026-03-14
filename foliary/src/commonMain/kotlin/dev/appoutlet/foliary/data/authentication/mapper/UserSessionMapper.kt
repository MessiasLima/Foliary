package dev.appoutlet.foliary.data.authentication.mapper

import dev.appoutlet.foliary.data.authentication.model.Session
import io.github.jan.supabase.auth.user.UserSession
import org.koin.core.annotation.Factory

@Factory
class UserSessionMapper {
    operator fun invoke(session: Session) = UserSession(
        accessToken = session.accessToken,
        refreshToken = session.refreshToken,
        providerRefreshToken = session.providerRefreshToken,
        providerToken = session.providerToken,
        expiresIn = session.expiresIn,
        tokenType = session.tokenType,
        type = session.type
    )
}
