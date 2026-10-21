package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.browser.BrowserLauncher
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import dev.mokkery.verifySuspend
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant
import kotlin.uuid.Uuid

class TaskDetailViewModelTest : ViewModelTest<TaskDetailViewModel, TaskDetailViewData, TaskDetailAction>() {
    private val taskId = Uuid.random()
    private val mockTaskRepository = mock<TaskRepository>(mode = MockMode.autoUnit)
    private val mockTaskViewDataMapper = mock<TaskViewDataMapper>()
    private val mockBrowserLauncher = mock<BrowserLauncher>(mode = MockMode.autoUnit)

    override fun createViewModel() = TaskDetailViewModel(
        taskId = taskId.toString(),
        taskRepository = mockTaskRepository,
        taskViewDataMapper = mockTaskViewDataMapper,
        browserLauncher = mockBrowserLauncher,
    )

    @Test
    fun `should load task and reduce to loaded state`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskViewDataMapper(task) } returns taskViewData

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
        every { mockTaskViewDataMapper(task) } returns taskViewData

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
        every { mockTaskViewDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.EditClicked)

            expectSideEffect(TaskDetailAction.NavigateToTaskEdit(taskId.toString()))
        }
    }

    @Test
    fun `should reflect completion changes emitted by repository`() {
        val incompleteTask = Task.fixture(id = taskId, completionDate = null)
        val completedTask = incompleteTask.copy(completionDate = Instant.parse("2026-10-21T10:00:00Z"))
        val incompleteViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(isComplete = false)
        val completedViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(
            isComplete = true,
            completionDate = "21 Oct 2026",
        )
        val tasks = MutableStateFlow<Task?>(incompleteTask)

        every { mockTaskRepository.observeById(taskId) } returns tasks
        every { mockTaskViewDataMapper(incompleteTask) } returns incompleteViewData
        every { mockTaskViewDataMapper(completedTask) } returns completedViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = incompleteViewData))

            tasks.value = completedTask

            expectState(TaskDetailViewData.Loaded(task = completedViewData))
        }
    }

    @Test
    fun `should mark task as completed when mark completed is clicked`() {
        val task = Task.fixture(id = taskId)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskViewDataMapper(task) } returns taskViewData

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
        every { mockTaskViewDataMapper(task) } returns taskViewData

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
        every { mockTaskViewDataMapper(task) } returns taskViewData

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.DeleteClicked)

            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.delete(taskId) }
    }

    @Test
    fun `should launch url in browser when url is clicked`() {
        val url = "https://example.com/page?q=1"
        val task = Task.fixture(id = taskId, url = url)
        val taskViewData = TaskDetailViewData.Loaded.TaskViewData.fixture(title = task.title)

        every { mockTaskRepository.observeById(taskId) } returns flowOf(task)
        every { mockTaskViewDataMapper(task) } returns taskViewData
        everySuspend { mockTaskRepository.getById(taskId) } returns task

        test {
            expectState(TaskDetailViewData.Loading)
            expectState(TaskDetailViewData.Loaded(task = taskViewData))

            viewModel.onEvent(TaskDetailEvent.UrlClicked)
            delay(1.milliseconds)

            verify { mockBrowserLauncher.launch(url) }
        }
    }
}
