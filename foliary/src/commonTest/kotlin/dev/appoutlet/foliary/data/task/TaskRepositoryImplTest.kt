package dev.appoutlet.foliary.data.task

import dev.appoutlet.foliary.data.task.database.TaskDao
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.appoutlet.foliary.data.time.TimeProvider
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Instant

class TaskRepositoryImplTest {
    private val mockTaskDao = mock<TaskDao>()
    private val mockTimeProvider = mock<TimeProvider>()
    private val subject = TaskRepositoryImpl(mockTaskDao, mockTimeProvider)

    @Test
    fun `should return tasks due today and overdue tasks`() = runTest {
        val endOfToday = Instant.parse("2026-07-22T23:59:59.999999999Z")
        val fixtureTasks = listOf(Task.fixture())

        every { mockTimeProvider.endOfToday() } returns endOfToday
        every { mockTaskDao.findTodayTasks(endOfToday) } returns flowOf(fixtureTasks)

        val result = subject.findTodayTasks().first()

        result shouldBe fixtureTasks
    }
}
