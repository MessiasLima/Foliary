package dev.appoutlet.foliary.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import dev.appoutlet.foliary.data.applicationversion.ApplicationVersionDao
import dev.appoutlet.foliary.data.applicationversion.model.ApplicationVersion

@Database(entities = [ApplicationVersion::class], version = 1)
@ConstructedBy(FoliaryDatabaseConstructor::class)
abstract class FoliaryDatabase : RoomDatabase() {
    abstract fun applicationVersionDao(): ApplicationVersionDao
}
