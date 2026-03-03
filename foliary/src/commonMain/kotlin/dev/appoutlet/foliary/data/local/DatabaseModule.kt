package dev.appoutlet.foliary.data.local

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule {
    @Single
    fun provideDatabase(): FoliaryDatabase {
        return getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Single
    fun provideApplicationVersionDao(database: FoliaryDatabase) = database.applicationVersionDao()
}
