package dev.appoutlet.foliary.data.authentication.model

fun Session.Companion.fixture(
    accessToken: String = "12312a908a0s9df8s0adf80s9df",
    refreshToken: String = "da98sda8s7da98d7asd",
    providerRefreshToken: String? = "f908sdf09sd8f",
    providerToken: String? = "98s7f98sd7fsd",
    expiresIn: Long = 123L,
    tokenType: String = "authentication",
    type: String = "session_type"
) = Session(
    accessToken = accessToken,
    refreshToken = refreshToken,
    providerRefreshToken = providerRefreshToken,
    providerToken = providerToken,
    expiresIn = expiresIn,
    tokenType = tokenType,
    type = type
)
