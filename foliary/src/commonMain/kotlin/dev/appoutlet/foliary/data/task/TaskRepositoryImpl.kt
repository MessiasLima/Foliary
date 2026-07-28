package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single
import org.koin.core.component.getScopeId
import kotlin.uuid.Uuid

@Single
class TaskRepositoryImpl(
    private val taskDao: TaskDao,
    private val timeProvider: TimeProvider,
) : TaskRepository {
    override fun findTodayTasks(): Flow<List<Task>> {
        val endOfToday = timeProvider.endOfToday()
        return taskDao.findTodayTasks(endOfToday)
    }

    override fun findById(id: Uuid) = taskDao.findById(id)

    override suspend fun save(task: Task) {
        taskDao.save(task)
    }

    override suspend fun delete(id: Uuid) {
        taskDao.delete(getById(id))
    }

    override suspend fun getById(id: Uuid) = taskDao.getById(id)

    override suspend fun markNotCompleted(id: Uuid) {
        val task = getById(id).copy(completionDate = null)
        save(task)
    }

    override suspend fun markCompleted(id: Uuid) {
        val task = getById(id).copy(completionDate = timeProvider.now())
        save(task)
    }
}
