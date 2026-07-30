package dev.appoutlet.foliary.data.task.database

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM Task")
    suspend fun findAll(): List<Task>

    @Query("SELECT * FROM Task WHERE id = :id")
    suspend fun getById(id: Uuid): Task

    @Query("SELECT * FROM Task WHERE id = :id")
    suspend fun findById(id: Uuid): Task?

    @Query("SELECT * FROM Task WHERE id = :id")
    fun observeById(id: Uuid): Flow<Task?>

    @Query(
        """
        SELECT *
        FROM Task
        WHERE (completionDate IS NULL AND (dueDate <= :endOfToday OR dueDate IS NULL))
            OR (completionDate IS NOT NULL AND completionDate >= :startOfToday AND completionDate <= :endOfToday)
        ORDER BY dueDate IS NULL, dueDate ASC
        """
    )
    fun findTodayTasks(startOfToday: Instant, endOfToday: Instant): Flow<List<Task>>
}
