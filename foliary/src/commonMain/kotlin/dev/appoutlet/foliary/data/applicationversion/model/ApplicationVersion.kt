package dev.appoutlet.foliary.data.applicationversion.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApplicationVersion(
    @PrimaryKey val buildNumber: Int,
    val versionName: String,
    val wasReleaseNotesShown: Boolean
)
