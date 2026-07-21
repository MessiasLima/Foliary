package dev.appoutlet.foliary.data.task.database

import dev.appoutlet.foliary.core.testing.DaoTest
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskDaoTest : DaoTest() {
    private val dao by lazy { database.taskDao() }

    @Test
    fun `should return overdue tasks and tasks due today`() = runTest {
        val endOfToday = Instant.parse("2026-07-21T23:59:59.999999999Z")

        val overdueTask = Task.fixture(
            title = "Overdue task",
            dueDate = Instant.parse("2026-07-20T22:00:00Z"),
            completionDate = null,
        )

        val dueTodayTask = Task.fixture(
            title = "Due today task",
            dueDate = Instant.parse("2026-07-21T11:00:00Z"),
            completionDate = null,
        )

        val dueAtEndOfDayTask = Task.fixture(
            title = "Due at end of day task",
            dueDate = Instant.parse("2026-07-21T23:59:59.999Z"),
            completionDate = null,
        )

        val futureTask = Task.fixture(
            title = "Future task",
            dueDate = endOfToday + 1.milliseconds,
            completionDate = null,
        )

        val completedTask = Task.fixture(
            title = "Completed task",
            dueDate = Instant.parse("2026-07-20T21:00:00Z"),
            completionDate = Instant.parse("2026-07-21T10:00:00Z"),
        )

        dao.save(overdueTask, dueTodayTask, dueAtEndOfDayTask, futureTask, completedTask)

        val result = dao.findTodayTasks(endOfToday).first()
        val resultIds = result.map { it.id }

        resultIds shouldBe listOf(dueAtEndOfDayTask.id, dueTodayTask.id, overdueTask.id)
    }
}
