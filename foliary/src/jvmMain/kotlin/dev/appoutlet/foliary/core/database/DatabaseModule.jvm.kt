@file:Suppress("Filename")

package dev.appoutlet.foliary.core.database

import androidx.room.Room
import androidx.room.RoomDatabase
import dev.appoutlet.foliary.core.appdirs.Directories
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import java.io.File

@Module
actual object PlatformDatabaseModule {
    @Single
    fun provideDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
        val databaseFolder = Directories.userDirectory
        val databaseFile = File(databaseFolder, "Foliary.db")
        return Room.databaseBuilder<FoliaryDatabase>(
            name = databaseFile.absolutePath,
        )
    }
}
