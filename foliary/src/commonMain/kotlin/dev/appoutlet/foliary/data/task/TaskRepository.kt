package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface TaskRepository {
    fun findTodayTasks(): Flow<List<Task>>
    fun findById(id: Uuid): Flow<Task?>
    suspend fun getById(id: Uuid): Task
    suspend fun save(task: Task)
    suspend fun delete(id: Uuid)
    suspend fun markNotCompleted(id: Uuid)
    suspend fun markCompleted(id: Uuid)
}
