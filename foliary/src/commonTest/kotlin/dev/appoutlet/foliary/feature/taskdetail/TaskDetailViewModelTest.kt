package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.capture.Capture
import dev.mokkery.matcher.capture.capture
import dev.mokkery.matcher.capture.get
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskDetailViewModelTest :
    ViewModelTest<TaskDetailViewModel, TaskDetailViewData, TaskDetailAction>() {
    private val taskId = Uuid.random()
    private val taskIdString = taskId.toString()
    private val mockTaskRepository = mock<TaskRepository>()
    private val mockTimeProvider = mock<TimeProvider>()

    override fun createViewModel() = TaskDetailViewModel(
        taskRepository = mockTaskRepository,
        timeProvider = mockTimeProvider,
    )

    @Test
    fun `should load task and reduce to loaded state`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))

        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = false))
    }

    @Test
    fun `should mark task as overdue when due date is before now`() = test {
        val now = Instant.parse("2026-07-21T12:00:00Z")
        val task = Task.fixture(
            id = taskId,
            dueDate = Instant.parse("2026-07-21T10:00:00Z"),
            completionDate = null,
        )

        every { mockTimeProvider.now() } returns now
        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))

        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = true))
    }

    @Test
    fun `should not mark completed task as overdue`() = test {
        val now = Instant.parse("2026-07-21T12:00:00Z")
        val task = Task.fixture(
            id = taskId,
            dueDate = Instant.parse("2026-07-21T10:00:00Z"),
            completionDate = Instant.parse("2026-07-21T11:00:00Z"),
        )

        every { mockTimeProvider.now() } returns now
        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))

        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = false))
    }

    @Test
    fun `should navigate back when back is clicked`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = false))

        viewModel.onEvent(TaskDetailEvent.BackClicked)

        expectSideEffect(TaskDetailAction.NavigateBack)
    }

    @Test
    fun `should mark task as completed`() = test {
        val now = Instant.parse("2026-07-21T12:00:00Z")
        val task = Task.fixture(
            id = taskId,
            completionDate = null,
        )
        val taskCapture = Capture.slot<Task>()

        every { mockTimeProvider.now() } returns now
        every { mockTaskRepository.findById(taskId) } returns flowOf(task)
        everySuspend { mockTaskRepository.save(capture(taskCapture)) } returns Unit

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = false))

        viewModel.onEvent(TaskDetailEvent.MarkCompletedClicked)

        expectSideEffect(TaskDetailAction.TaskMarkedCompleted)

        taskCapture.get().completionDate shouldBe now
        verifySuspend { mockTaskRepository.save(taskCapture.get()) }
    }

    @Test
    fun `should delete task and navigate back`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)
        everySuspend { mockTaskRepository.delete(taskId) } returns Unit

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = task, isOverdue = false))

        viewModel.onEvent(TaskDetailEvent.DeleteClicked)

        expectSideEffect(TaskDetailAction.NavigateBack)

        verifySuspend { mockTaskRepository.delete(taskId) }
    }
}
