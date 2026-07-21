package dev.appoutlet.foliary.core.database.typeconverter

import androidx.room3.TypeConverter
import kotlin.uuid.Uuid

class UuidTypeConverter {
    @TypeConverter
    fun fromUuid(uuid: Uuid): String = uuid.toString()

    @TypeConverter
    fun toUuid(string: String) = Uuid.parse(string)
}
