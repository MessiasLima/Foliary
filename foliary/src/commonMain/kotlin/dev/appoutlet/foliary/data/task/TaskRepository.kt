package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun findTodayTasks(): Flow<List<Task>>
    suspend fun save(task: Task)
}
