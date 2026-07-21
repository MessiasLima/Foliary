package dev.appoutlet.foliary.data.task.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg task: Task)

    @Query("SELECT * FROM Task WHERE dueDate > :today ORDER BY dueDate DESC")
    fun findTodayTasks(today: Instant): Flow<List<Task>>
}
