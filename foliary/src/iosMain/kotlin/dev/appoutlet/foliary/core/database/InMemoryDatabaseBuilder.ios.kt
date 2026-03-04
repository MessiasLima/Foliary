package dev.appoutlet.foliary.core.database

import androidx.room.Room
import androidx.room.RoomDatabase

actual fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    return Room.inMemoryDatabaseBuilder()
}