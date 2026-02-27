package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.domain.Task

class TaskRepositoryImpl : TaskRepository {
    override suspend fun findTodaysTasks(): List<Task> {
        return emptyList()
    }
}