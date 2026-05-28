package dev.appoutlet.foliary.data.authentication.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class Session(
    @PrimaryKey val accessToken: String,
    val refreshToken: String,
    val providerRefreshToken: String?,
    val providerToken: String?,
    val expiresIn: Long,
    val tokenType: String,
    val type: String,
) {
    companion object
}
