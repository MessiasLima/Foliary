package dev.appoutlet.foliary.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.appoutlet.foliary.data.local.entity.ApplicationVersion
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationVersionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(applicationVersion: ApplicationVersion)

    @Update
    suspend fun update(applicationVersion: ApplicationVersion)

    @Query("SELECT * FROM ApplicationVersion")
    fun getAll(): Flow<List<ApplicationVersion>>

    @Query("SELECT * FROM ApplicationVersion WHERE buildNumber = :buildNumber")
    suspend fun getByBuildNumber(buildNumber: Int): ApplicationVersion?
}
