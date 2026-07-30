package dev.appoutlet.foliary.data.task.database

import dev.appoutlet.foliary.core.testing.DaoTest
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAllInAnyOrder
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskDaoTest : DaoTest() {
    private val dao by lazy { database.taskDao() }

    @Test
    fun `should delete task by id`() = runTest {
        val task = Task.fixture(title = "Task to delete")
        val otherTask = Task.fixture(title = "Other task")

        dao.save(task, otherTask)

        dao.delete(task)

        dao.findById(task.id) shouldBe null
        dao.findAll().map { it.id } shouldContain otherTask.id
    }

    @Test
    fun `should find all`() = runTest {
        val task1 = Task.fixture(
            title = "Task 1",
            dueDate = Instant.parse("2026-07-20T22:00:00Z"),
            completionDate = null,
        )

        val task2 = Task.fixture(
            title = "Task 2",
            dueDate = Instant.parse("2026-07-21T11:00:00Z"),
            completionDate = null,
        )

        val task3 = Task.fixture(
            title = "Task 3",
            dueDate = Instant.parse("2026-07-21T23:59:59.999Z"),
            completionDate = null,
        )

        dao.save(task1, task2, task3)

        val allTasks = dao.findAll()
        val savedTasksTitles = allTasks.map { it.title }

        savedTasksTitles shouldContainAllInAnyOrder listOf(
            task1.title,
            task2.title,
            task3.title,
        )
    }

    @Test
    fun `should return today's tasks`() = runTest {
        val startOfToday = Instant.parse("2026-07-21T00:00:00Z")
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

        val noDueDateTask = Task.fixture(
            title = "No due date task",
            dueDate = null,
            completionDate = null,
        )

        val completedTodayTask = Task.fixture(
            title = "Completed today task",
            dueDate = Instant.parse("2026-07-20T21:00:00Z"),
            completionDate = Instant.parse("2026-07-21T10:00:00Z"),
        )

        val completedYesterdayTask = Task.fixture(
            title = "Completed yesterday task",
            dueDate = Instant.parse("2026-07-20T21:00:00Z"),
            completionDate = Instant.parse("2026-07-20T10:00:00Z"),
        )

        val completedTomorrowTask = Task.fixture(
            title = "Completed tomorrow task",
            dueDate = Instant.parse("2026-07-20T21:00:00Z"),
            completionDate = Instant.parse("2026-07-22T10:00:00Z"),
        )

        dao.save(
            overdueTask,
            dueTodayTask,
            dueAtEndOfDayTask,
            futureTask,
            noDueDateTask,
            completedTodayTask,
            completedYesterdayTask,
            completedTomorrowTask,
        )

        val result = dao.findTodayTasks(startOfToday, endOfToday).first()
        val resultIds = result.map { it.id }

        resultIds shouldBe listOf(
            completedTodayTask.id,
            overdueTask.id,
            dueTodayTask.id,
            dueAtEndOfDayTask.id,
            noDueDateTask.id,
        )
        // TODO make sure the tasks appear on the correct order
    }

    @Test
    fun `should return task by id`() = runTest {
        val task = Task.fixture(title = "Target task")
        val otherTask = Task.fixture(title = "Other task")

        dao.save(task, otherTask)

        val result = dao.getById(task.id)

        result.id shouldBe task.id
        result.title shouldBe task.title
    }

    @Test
    fun `should throw when task by id does not exist`() = runTest {
        shouldThrowAny { dao.getById(Uuid.random()) }
    }

    @Test
    fun `should return null when finding task by id that does not exist`() = runTest {
        val result = dao.findById(Uuid.random())

        result shouldBe null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should observe task by id`() = runTest {
        val task = Task.fixture(title = "Target task")
        val otherTask = Task.fixture(title = "Other task")

        dao.save(task, otherTask)

        val emissions = Channel<Task?>(Channel.UNLIMITED)
        val job = launch { dao.observeById(task.id).collect { emissions.send(it) } }

        emissions.receive().shouldNotBeNull().title shouldBe "Target task"

        dao.save(task.copy(title = "Updated task"))
        advanceUntilIdle()

        emissions.receive().shouldNotBeNull().title shouldBe "Updated task"
        job.cancel()
    }
}
