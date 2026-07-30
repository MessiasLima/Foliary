package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Instant

class TaskRepositoryImplTest {
    private val mockTaskDao = mock<TaskDao>(mode = MockMode.autoUnit)
    private val mockTimeProvider = mock<TimeProvider>()
    private val subject = TaskRepositoryImpl(mockTaskDao, mockTimeProvider)

    @Test
    fun `should return today's tasks`() = runTest {
        val startOfToday = Instant.parse("2026-07-22T00:00:00Z")
        val endOfToday = Instant.parse("2026-07-22T23:59:59.999999999Z")
        val fixtureTasks = listOf(Task.fixture())

        every { mockTimeProvider.startOfToday() } returns startOfToday
        every { mockTimeProvider.endOfToday() } returns endOfToday
        every { mockTaskDao.findTodayTasks(startOfToday, endOfToday) } returns flowOf(fixtureTasks)

        val result = subject.findTodayTasks().first()

        result shouldBe fixtureTasks
    }

    @Test
    fun `should observe task by id`() = runTest {
        val task = Task.fixture()

        every { mockTaskDao.observeById(task.id) } returns flowOf(task)

        subject.observeById(task.id).first() shouldBe task
    }

    @Test
    fun `should save task`() = runTest {
        val task = Task.fixture()

        subject.save(task)

        verifySuspend { mockTaskDao.save(task) }
    }

    @Test
    fun `should delete task by id`() = runTest {
        val task = Task.fixture()

        everySuspend { mockTaskDao.getById(task.id) } returns task

        subject.delete(task.id)

        verifySuspend { mockTaskDao.delete(task) }
    }

    @Test
    fun `should get task by id`() = runTest {
        val task = Task.fixture()

        everySuspend { mockTaskDao.getById(task.id) } returns task

        subject.getById(task.id) shouldBe task
    }

    @Test
    fun `should find task by id`() = runTest {
        val task = Task.fixture()

        everySuspend { mockTaskDao.findById(task.id) } returns task

        subject.findById(task.id) shouldBe task
    }

    @Test
    fun `should mark task as not completed`() = runTest {
        val task = Task.fixture(completionDate = Instant.parse("2026-07-22T10:00:00Z"))
        val expectedTask = task.copy(completionDate = null)

        everySuspend { mockTaskDao.getById(task.id) } returns task

        subject.markNotCompleted(task.id)

        verifySuspend { mockTaskDao.save(expectedTask) }
    }

    @Test
    fun `should mark task as completed`() = runTest {
        val now = Instant.parse("2026-07-22T12:00:00Z")
        val task = Task.fixture(completionDate = null)
        val expectedTask = task.copy(completionDate = now)

        every { mockTimeProvider.now() } returns now
        everySuspend { mockTaskDao.getById(task.id) } returns task

        subject.markCompleted(task.id)

        verifySuspend { mockTaskDao.save(expectedTask) }
    }
}
