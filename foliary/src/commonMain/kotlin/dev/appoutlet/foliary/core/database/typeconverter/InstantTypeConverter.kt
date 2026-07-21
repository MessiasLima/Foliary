package dev.appoutlet.foliary.core.database.typeconverter

import androidx.room3.TypeConverter
import kotlin.time.Instant

class InstantTypeConverter {
    @TypeConverter
    fun toInstant(epochMilliseconds: Long) = Instant.fromEpochMilliseconds(epochMilliseconds)

    @TypeConverter
    fun fromInstant(instant: Instant) = instant.toEpochMilliseconds()
}
