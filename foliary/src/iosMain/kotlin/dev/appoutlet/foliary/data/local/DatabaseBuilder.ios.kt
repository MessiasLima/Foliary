package dev.appoutlet.foliary.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSHomeDirectory

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val dbFilePath = NSHomeDirectory() + "/foliary.db"
    return Room.databaseBuilder<FoliaryDatabase>(
        name = dbFilePath,
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}

actual fun getInMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>? {
    return Room.inMemoryDatabaseBuilder<FoliaryDatabase>(
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}
