package dev.appoutlet.foliary.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val dbFilePath = documentDirectory() + "/foliary.db"
    return Room.databaseBuilder<FoliaryDatabase>(
        name = dbFilePath,
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}

private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}

actual fun getInMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>? {
    return Room.inMemoryDatabaseBuilder<FoliaryDatabase>(
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}
