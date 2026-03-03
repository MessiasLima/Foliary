package dev.appoutlet.foliary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.appoutlet.foliary.data.local.dao.ApplicationVersionDao
import dev.appoutlet.foliary.data.local.entity.ApplicationVersion

@Database(entities = [ApplicationVersion::class], version = 1)
abstract class FoliaryDatabase : RoomDatabase() {
    abstract fun applicationVersionDao(): ApplicationVersionDao
}
