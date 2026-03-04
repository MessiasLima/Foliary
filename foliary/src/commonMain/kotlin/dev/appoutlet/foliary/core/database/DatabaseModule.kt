package dev.appoutlet.foliary.core.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(includes = [PlatformDatabaseModule::class])
@Configuration
class DatabaseModule {
    @Single
    fun provideDatabase(databaseBuilder: RoomDatabase.Builder<FoliaryDatabase>): FoliaryDatabase {
        return databaseBuilder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Single
    fun provideApplicationVersionDao(database: FoliaryDatabase) = database.applicationVersionDao()
}

@Module
expect object PlatformDatabaseModule