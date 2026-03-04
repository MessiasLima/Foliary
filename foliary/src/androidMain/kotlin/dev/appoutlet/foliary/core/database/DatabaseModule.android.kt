@file:Suppress("Filename", "MatchingDeclarationName")

package dev.appoutlet.foliary.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
actual object PlatformDatabaseModule {
    @Single
    fun provideDatabaseBuilder(context: Context): RoomDatabase.Builder<FoliaryDatabase> {
        val dbFile = context.applicationContext.getDatabasePath("foliary.db")
        return Room.databaseBuilder<FoliaryDatabase>(
            context = context,
            name = dbFile.absolutePath,
            factory = { FoliaryDatabaseConstructor.initialize() }
        )
    }
}
