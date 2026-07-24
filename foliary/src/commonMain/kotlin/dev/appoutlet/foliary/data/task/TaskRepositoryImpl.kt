package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
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
