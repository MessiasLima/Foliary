package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.time.TimeProvider
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val timeProvider: TimeProvider,
) : TaskRepository {
    override fun findTodayTasks(): Flow<List<Task>> {
        val endOfToday = timeProvider.endOfToday()
        return taskDao.findTodayTasks(endOfToday)
    }

    override suspend fun save(task: Task) {
        taskDao.save(task)
    }
}
