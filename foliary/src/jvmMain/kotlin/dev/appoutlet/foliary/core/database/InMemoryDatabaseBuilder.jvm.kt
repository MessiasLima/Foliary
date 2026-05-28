package dev.appoutlet.foliary.core.database

import androidx.room3.Room
import androidx.room3.RoomDatabase

actual fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    return Room.inMemoryDatabaseBuilder()
}
