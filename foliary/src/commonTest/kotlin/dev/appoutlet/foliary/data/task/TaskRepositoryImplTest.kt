package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.core.testing.DaoTest
import dev.appoutlet.foliary.data.task.database.entity.Task
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.Uuid

class TaskRepositoryImplTest : DaoTest() {
    private val taskDao by lazy { database.taskDao() }
    private val subject by lazy { TaskRepositoryImpl(taskDao) }

    @Test
    fun `should return tasks due today and overdue tasks`() = runTest {
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val todayStart = now.toLocalDateTime(timeZone).date
            .atTime(0, 0)
            .toInstant(timeZone)
        val tomorrowStart = todayStart + 1.days

        val overdueTask = task(
            title = "Overdue task",
            dueDate = todayStart - 1.hours,
        )
        val dueTodayTask = task(
            title = "Due today task",
            dueDate = todayStart + 12.hours,
        )
        val dueAtEndOfDayTask = task(
            title = "Due at end of day task",
            dueDate = tomorrowStart - 1.milliseconds,
        )
        val futureTask = task(
            title = "Future task",
            dueDate = tomorrowStart,
        )
        val completedTask = task(
            title = "Completed task",
            dueDate = todayStart - 2.hours,
            completionDate = now,
        )

        taskDao.save(overdueTask, dueTodayTask, dueAtEndOfDayTask, futureTask, completedTask)

        val result = subject.findTodayTasks().first()

        result shouldBe listOf(dueAtEndOfDayTask, dueTodayTask, overdueTask)
    }

    private fun task(
        title: String,
        dueDate: kotlin.time.Instant,
        completionDate: kotlin.time.Instant? = null,
    ) = Task(
        id = Uuid.random(),
        title = title,
        description = null,
        creationDate = dueDate,
        dueDate = dueDate,
        completionDate = completionDate,
        priority = null,
        url = null,
        location = null,
    )
}
