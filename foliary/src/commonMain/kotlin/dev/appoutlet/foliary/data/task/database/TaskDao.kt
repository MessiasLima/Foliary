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

    @Query("SELECT * FROM Task")
    suspend fun findAll(): List<Task>

    @Query(
        """
        SELECT *
        FROM Task
        WHERE (dueDate <= :endOfToday OR dueDate IS NULL)
            AND completionDate IS NULL
        ORDER BY dueDate IS NULL, dueDate ASC
    """
    )
    fun findTodayTasks(endOfToday: Instant): Flow<List<Task>>
}
