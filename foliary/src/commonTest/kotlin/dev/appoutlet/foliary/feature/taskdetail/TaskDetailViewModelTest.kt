package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.uuid.Uuid

class TaskDetailViewModelTest :
    ViewModelTest<TaskDetailViewModel, TaskDetailViewData, TaskDetailAction>() {
    private val taskId = Uuid.random()
    private val taskIdString = taskId.toString()
    private val mockTaskRepository = mock<TaskRepository>()

    override fun createViewModel() = TaskDetailViewModel(
        taskRepository = mockTaskRepository,
    )

    @Test
    fun `should load task and reduce to loaded state`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))

        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = TaskDetailViewData.Loaded.TaskViewData(task.title)))
    }

    @Test
    fun `should navigate back when back is clicked`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = TaskDetailViewData.Loaded.TaskViewData(task.title)))

        viewModel.onEvent(TaskDetailEvent.BackClicked)

        expectSideEffect(TaskDetailAction.NavigateBack)
    }

    @Test
    fun `should navigate back when mark completed is clicked`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = TaskDetailViewData.Loaded.TaskViewData(task.title)))

        viewModel.onEvent(TaskDetailEvent.MarkCompletedClicked)

        expectSideEffect(TaskDetailAction.NavigateBack)
    }

    @Test
    fun `should navigate back when delete is clicked`() = test {
        val task = Task.fixture(id = taskId)

        every { mockTaskRepository.findById(taskId) } returns flowOf(task)

        viewModel.onEvent(TaskDetailEvent.LoadTask(taskIdString))
        expectState(TaskDetailViewData.Loading)
        expectState(TaskDetailViewData.Loaded(task = TaskDetailViewData.Loaded.TaskViewData(task.title)))

        viewModel.onEvent(TaskDetailEvent.DeleteClicked)

        expectSideEffect(TaskDetailAction.NavigateBack)
    }
}
