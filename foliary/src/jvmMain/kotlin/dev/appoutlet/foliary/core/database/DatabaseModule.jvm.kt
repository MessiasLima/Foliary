package dev.appoutlet.foliary.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import ca.gosyer.appdirs.AppDirs
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.io.File

@Module
actual object PlatformDatabaseModule {
    @Single
    fun provideDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
        val databaseFolder = getUserDataFolder()
        createFolderIfItDoesntExists(databaseFolder)
        val dbFile = File(databaseFolder, "Foliary.db")
        return Room.databaseBuilder<FoliaryDatabase>(
            name = dbFile.absolutePath,
        )
    }

    private fun getUserDataFolder(): String {
        val appDirs = AppDirs {
            appName = "Foliary"
            appAuthor = "AppOutlet"
        }

        return appDirs.getUserDataDir(roaming = true)
    }

    private fun createFolderIfItDoesntExists(databaseFolder: String) {
        val file = File(databaseFolder)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}