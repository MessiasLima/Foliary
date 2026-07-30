package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.core.testing.externalfixtures.fixture
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.core.ui.component.task.fixture
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.data.authentication.util.name
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.uuid.Uuid

class TodayViewModelTest : ViewModelTest<TodayViewModel, TodayViewData, TodayAction>() {
    private val mockTaskRepository = mock<TaskRepository>()
    private val mockAuthenticationRepository = mock<AuthenticationRepository>()
    private val mockFoliaryTaskCardViewDataMapper = mock<FoliaryTaskCardViewDataMapper>()

    override fun createViewModel() = TodayViewModel(
        taskRepository = mockTaskRepository,
        authenticationRepository = mockAuthenticationRepository,
        foliaryTaskCardViewDataMapper = mockFoliaryTaskCardViewDataMapper
    )

    @Test
    fun `should show empty when there are no tasks for today`() {
        val fixture = UserInfo.fixture()
        every { mockAuthenticationRepository.currentUser() } returns fixture
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = fixture.name()!!, completedTasks = emptyList()))
        }
    }

    @Test
    fun `should show empty with blank user name when user is not authenticated`() {
        every { mockAuthenticationRepository.currentUser() } returns null
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "", completedTasks = emptyList()))
        }
    }

    @Test
    fun `should split tasks into pending and completed lists`() {
        val pendingTask = Task.fixture(completionDate = null)
        val completedTask = Task.fixture()
        val pendingViewData = FoliaryTaskCardViewData.fixture(
            id = pendingTask.id.toString(),
            isCompleted = false
        )
        val completedViewData = FoliaryTaskCardViewData.fixture(
            id = completedTask.id.toString(),
            isCompleted = true
        )

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(pendingTask) } returns pendingViewData
        every { mockFoliaryTaskCardViewDataMapper(completedTask) } returns completedViewData
        every { mockTaskRepository.findTodayTasks() } returns flowOf(listOf(pendingTask, completedTask))

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingViewData),
                    completedTasks = listOf(completedViewData)
                )
            )
        }
    }

    @Test
    fun `should show empty when all tasks are completed on initial load`() {
        val task = Task.fixture()
        val taskViewData = FoliaryTaskCardViewData.fixture(id = task.id.toString(), isCompleted = true)

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(task) } returns taskViewData
        every { mockTaskRepository.findTodayTasks() } returns flowOf(listOf(task))

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Empty(
                    userName = "Messias",
                    completedTasks = listOf(taskViewData)
                )
            )
        }
    }

    @Test
    fun `should transition to celebration when last pending task is marked completed`() {
        val taskId = Uuid.parse("550e8400-e29b-41d4-a716-446655440000")
        val pendingTask = Task.fixture(id = taskId, completionDate = null)
        val completedTask = pendingTask.copy(completionDate = pendingTask.creationDate)
        val completedTaskViewData = FoliaryTaskCardViewData.fixture(
            id = pendingTask.id.toString(),
            isCompleted = true
        )
        val pendingTaskViewData = FoliaryTaskCardViewData.fixture(
            id = pendingTask.id.toString(),
            isCompleted = false
        )
        val tasksFlow = MutableStateFlow(listOf(pendingTask))

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(pendingTask) } returns pendingTaskViewData
        every { mockFoliaryTaskCardViewDataMapper(completedTask) } returns completedTaskViewData
        every { mockTaskRepository.findTodayTasks() } returns tasksFlow
        everySuspend { mockTaskRepository.markCompleted(taskId) } returns Unit

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingTaskViewData),
                    completedTasks = emptyList()
                )
            )

            viewModel.onEvent(TodayEvent.MarkTaskAsCompleted(pendingTask.id.toString()))
            delay(1.milliseconds)
            tasksFlow.value = listOf(completedTask)

            expectState(
                TodayViewData.Celebration(
                    userName = "Messias",
                    completedTasks = listOf(completedTaskViewData)
                )
            )
        }
    }

    @Test
    fun `should transition back to loaded when a task is marked not completed from celebration`() {
        val taskId = Uuid.parse("550e8400-e29b-41d4-a716-446655440000")
        val pendingTask = Task.fixture(id = taskId, completionDate = null)
        val completedTask = pendingTask.copy(completionDate = pendingTask.creationDate)
        val completedTaskViewData = FoliaryTaskCardViewData.fixture(
            id = completedTask.id.toString(),
            isCompleted = true
        )
        val pendingTaskViewData = FoliaryTaskCardViewData.fixture(
            id = pendingTask.id.toString(),
            isCompleted = false
        )
        val tasksFlow = MutableStateFlow(listOf(pendingTask))

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(pendingTask) } returns pendingTaskViewData
        every { mockFoliaryTaskCardViewDataMapper(completedTask) } returns completedTaskViewData
        every { mockTaskRepository.findTodayTasks() } returns tasksFlow
        everySuspend { mockTaskRepository.markCompleted(taskId) } returns Unit
        everySuspend { mockTaskRepository.markNotCompleted(taskId) } returns Unit

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingTaskViewData),
                    completedTasks = emptyList()
                )
            )

            viewModel.onEvent(TodayEvent.MarkTaskAsCompleted(pendingTask.id.toString()))
            delay(1.milliseconds)
            tasksFlow.value = listOf(completedTask)

            expectState(
                TodayViewData.Celebration(
                    userName = "Messias",
                    completedTasks = listOf(completedTaskViewData)
                )
            )

            viewModel.onEvent(TodayEvent.MarkTaskAsNotCompleted(completedTask.id.toString()))
            delay(1.milliseconds)
            tasksFlow.value = listOf(pendingTask)

            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingTaskViewData),
                    completedTasks = emptyList()
                )
            )
        }
    }

    @Test
    fun `should transition back to loaded when a new uncompleted task becomes due today`() {
        val completedTask = Task.fixture()
        val newPendingTask = Task.fixture(
            id = Uuid.parse("550e8400-e29b-41d4-a716-446655440001"),
            completionDate = null
        )
        val completedTaskViewData = FoliaryTaskCardViewData.fixture(
            id = completedTask.id.toString(),
            isCompleted = true
        )
        val pendingTaskViewData = FoliaryTaskCardViewData.fixture(
            id = newPendingTask.id.toString(),
            isCompleted = false
        )
        val tasksFlow = MutableStateFlow(listOf(completedTask))

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(completedTask) } returns completedTaskViewData
        every { mockFoliaryTaskCardViewDataMapper(newPendingTask) } returns pendingTaskViewData
        every { mockTaskRepository.findTodayTasks() } returns tasksFlow

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Empty(
                    userName = "Messias",
                    completedTasks = listOf(completedTaskViewData)
                )
            )

            tasksFlow.value = listOf(completedTask, newPendingTask)

            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    pendingTasks = listOf(pendingTaskViewData),
                    completedTasks = listOf(completedTaskViewData)
                )
            )
        }
    }

    @Test
    fun `should call markCompleted when MarkTaskAsCompleted is emitted`() {
        val taskId = Uuid.parse("550e8400-e29b-41d4-a716-446655440000")

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())
        everySuspend { mockTaskRepository.markCompleted(taskId) } returns Unit

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "Messias", completedTasks = emptyList()))

            viewModel.onEvent(TodayEvent.MarkTaskAsCompleted(taskId.toString()))
            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.markCompleted(taskId) }
    }

    @Test
    fun `should call markNotCompleted when MarkTaskAsNotCompleted is emitted`() {
        val taskId = Uuid.parse("550e8400-e29b-41d4-a716-446655440000")

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())
        everySuspend { mockTaskRepository.markNotCompleted(taskId) } returns Unit

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "Messias", completedTasks = emptyList()))

            viewModel.onEvent(TodayEvent.MarkTaskAsNotCompleted(taskId.toString()))
            delay(1.milliseconds)
        }

        verifySuspend { mockTaskRepository.markNotCompleted(taskId) }
    }

    @Test
    fun `should navigate to create task when add task is clicked`() {
        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "Messias", completedTasks = emptyList()))

            viewModel.onEvent(TodayEvent.OnAddTaskClick)

            expectSideEffect(TodayAction.NavigateToCreateTask)
        }
    }

    @Test
    fun `should navigate to task detail when task is clicked`() {
        val taskId = "task-id"

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "Messias", completedTasks = emptyList()))

            viewModel.onEvent(TodayEvent.OnTaskClick(taskId))

            expectSideEffect(TodayAction.NavigateToTaskDetail(taskId))
        }
    }
}
