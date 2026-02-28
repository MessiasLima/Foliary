package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.domain.Task

interface TaskRepository {
    suspend fun findTodaysTasks(): List<Task>
}
