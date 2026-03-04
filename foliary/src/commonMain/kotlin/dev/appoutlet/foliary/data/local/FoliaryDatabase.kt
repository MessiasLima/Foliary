package dev.appoutlet.foliary.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import dev.appoutlet.foliary.data.local.dao.ApplicationVersionDao
import dev.appoutlet.foliary.data.local.entity.ApplicationVersion

@Database(entities = [ApplicationVersion::class], version = 1)
@ConstructedBy(FoliaryDatabaseConstructor::class)
abstract class FoliaryDatabase : RoomDatabase() {
    abstract fun applicationVersionDao(): ApplicationVersionDao
}

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "NO_ACTUAL_FOR_EXPECT")
expect object FoliaryDatabaseConstructor : RoomDatabaseConstructor<FoliaryDatabase> {
    override fun initialize(): FoliaryDatabase
}
