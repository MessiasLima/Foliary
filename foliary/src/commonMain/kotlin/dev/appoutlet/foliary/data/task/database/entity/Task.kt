package dev.appoutlet.foliary.data.task.database.entity

import androidx.room3.Embedded
import androidx.room3.Entity
import androidx.room3.PrimaryKey
import kotlinx.serialization.internal.NamedCompanion
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity
data class Task(
    @PrimaryKey val id: Uuid,
    val title: String,
    val description: String?,
    val creationDate: Instant,
    val dueDate: Instant?,
    val completionDate: Instant?,
    val priority: Priority?,
    val url: String?,
    @Embedded val location: Location?,
) {
    companion object
}
