package dev.appoutlet.foliary.data.applicationversion.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class ApplicationVersion(
    @PrimaryKey val buildNumber: Int,
    val versionName: String,
    val wasReleaseNotesShown: Boolean
)
