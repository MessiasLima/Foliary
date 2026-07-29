package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.Uuid

class TaskDetailViewModelTest : ViewModelTest<TaskDetailViewModel, TaskDetailViewData, TaskDetailAction>() {
    private val taskId = Uuid.random()
    private val mockTaskRepository = mock<TaskRepository>(mode = MockMode.autoUnit)
    private val mockTaskDataMapper = mock<TaskDataMapper>()

    override fun createViewModel() = TaskDetailViewModel(
        taskId = taskId.toString(),
        taskRepository = mockTaskRepository,
        taskViewDataMapper = mockTaskDataMapper,
    )

    @Test
    fun `should load task and reduce to loaded state`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))
        }
    }

    @Test
    fun `should navigate back when back is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.BackClicked)

            expectSideEffect(TaskDetailAction.NavigateBack)
        }
    }

    @Test
    fun `should navigate back when task is not found`() {
        every { mockTaskRepository.observeById(taskId) } returns flowOf(null)

        test {
            expectState(TaskDetailViewData.Loading)
            expectSideEffect(TaskDetailAction.NavigateBack)
        }
    }

    @Test
    fun `should navigate to task edit when edit is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.EditClicked)

            expectSideEffect(TaskDetailAction.NavigateToTaskEdit(taskId.toString()))
        }
    }

    @Test
    fun `should mark task as completed when mark completed is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.MarkCompletedClicked)

            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.markCompleted(taskId) }
    }

    @Test
    fun `should mark task as not completed when mark not completed is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.MarkNotCompletedClicked)

            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.markNotCompleted(taskId) }
    }

    @Test
    fun `should delete task when delete is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.DeleteClicked)

            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.delete(taskId) }
    }
}
