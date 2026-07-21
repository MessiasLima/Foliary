package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override fun findTodayTasks(): Flow<List<Task>> {
        val timeZone = TimeZone.currentSystemDefault()

        val today = Clock.System.now()
            .toLocalDateTime(timeZone)
            .date
            .atStartOfDayIn(timeZone)

        return taskDao.findTodayTasks(today)
    }
}
