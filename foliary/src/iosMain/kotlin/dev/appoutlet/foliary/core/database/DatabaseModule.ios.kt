package dev.appoutlet.foliary.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
@Module
actual object PlatformDatabaseModule {
    @Single
    private fun provideDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
        val dbFilePath = documentDirectory() + "/foliary.db"
        return Room.databaseBuilder<FoliaryDatabase>(dbFilePath)
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
}