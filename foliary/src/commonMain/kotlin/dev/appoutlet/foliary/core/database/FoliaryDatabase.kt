package dev.appoutlet.foliary.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import dev.appoutlet.foliary.data.applicationversion.ApplicationVersionDao
import dev.appoutlet.foliary.data.applicationversion.model.ApplicationVersion
import dev.appoutlet.foliary.data.authentication.database.SessionDao
import dev.appoutlet.foliary.data.authentication.model.Session

@Database(entities = [ApplicationVersion::class, Session::class], version = 1)
@ConstructedBy(FoliaryDatabaseConstructor::class)
abstract class FoliaryDatabase : RoomDatabase() {
    abstract fun applicationVersionDao(): ApplicationVersionDao
    abstract fun sessionDao(): SessionDao
}
