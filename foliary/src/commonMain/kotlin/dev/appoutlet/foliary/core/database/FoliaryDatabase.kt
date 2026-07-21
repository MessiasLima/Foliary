package dev.appoutlet.foliary.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import dev.appoutlet.foliary.core.database.typeconverter.InstantTypeConverter
import dev.appoutlet.foliary.core.database.typeconverter.UuidTypeConverter
import dev.appoutlet.foliary.data.applicationversion.ApplicationVersionDao
import dev.appoutlet.foliary.data.applicationversion.model.ApplicationVersion
import dev.appoutlet.foliary.data.authentication.database.SessionDao
import dev.appoutlet.foliary.data.authentication.model.Session
import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task

@Database(entities = [ApplicationVersion::class, Session::class, Task::class], version = 1)
@ConstructedBy(FoliaryDatabaseConstructor::class)
@TypeConverters(UuidTypeConverter::class, InstantTypeConverter::class)
abstract class FoliaryDatabase : RoomDatabase() {
    abstract fun applicationVersionDao(): ApplicationVersionDao
    abstract fun sessionDao(): SessionDao
    abstract fun taskDao(): TaskDao
}
