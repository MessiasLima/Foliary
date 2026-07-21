package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

@Single
class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override fun findTodayTasks(): Flow<List<Task>> {
        val timeZone = TimeZone.currentSystemDefault()

        val now = Clock.System.now()
        val cutoff = now.toLocalDateTime(timeZone).date
            .atTime(0, 0)
            .toInstant(timeZone) + 1.days

        return taskDao.findTodayTasks(cutoff)
    }
}
