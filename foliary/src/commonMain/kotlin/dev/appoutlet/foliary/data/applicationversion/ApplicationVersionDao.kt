package dev.appoutlet.foliary.data.applicationversion

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.appoutlet.foliary.data.applicationversion.model.ApplicationVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationVersionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(applicationVersion: ApplicationVersion)

    @Query("SELECT * FROM ApplicationVersion")
    fun getAll(): Flow<List<ApplicationVersion>>

    @Query("SELECT * FROM ApplicationVersion WHERE buildNumber = :buildNumber")
    suspend fun findByBuildNumber(buildNumber: Int): ApplicationVersion?
}