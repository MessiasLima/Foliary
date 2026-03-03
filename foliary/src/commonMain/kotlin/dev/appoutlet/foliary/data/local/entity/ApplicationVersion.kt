package dev.appoutlet.foliary.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ApplicationVersion(
    @PrimaryKey val buildNumber: Int,
    val versionName: String,
    val wasReleaseNotesShown: Boolean
)
