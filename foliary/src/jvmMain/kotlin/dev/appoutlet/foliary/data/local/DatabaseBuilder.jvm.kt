package dev.appoutlet.foliary.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".foliary/foliary.db")
    if (!dbFile.parentFile.exists()) {
        dbFile.parentFile.mkdirs()
    }
    return Room.databaseBuilder<FoliaryDatabase>(
        name = dbFile.absolutePath,
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}



actual fun getInMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    return Room.inMemoryDatabaseBuilder<FoliaryDatabase>(
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}
