package dev.appoutlet.foliary.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import ca.gosyer.appdirs.AppDirs
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val databaseFolder = getUserDataFolder()
    createFolderIfItDoesntExists(databaseFolder)
    val dbFile = File(databaseFolder, "foliary.db")
    return Room.databaseBuilder<FoliaryDatabase>(
        name = dbFile.absolutePath,
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}

private fun getUserDataFolder(): String {
    val appDirs = AppDirs {
        appName = "Foliary"
        appAuthor = "AppOutlet"
    }
    return appDirs.getUserDataDir()
}

private fun createFolderIfItDoesntExists(databaseFolder: String) {
    val file = File(databaseFolder)
    if (!file.exists()) {
        file.mkdirs()
    }
}

actual fun getInMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>? {
    return Room.inMemoryDatabaseBuilder<FoliaryDatabase>(
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}
