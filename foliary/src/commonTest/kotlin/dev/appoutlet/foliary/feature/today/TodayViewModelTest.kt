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
import dev.mokkery.mock
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test

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
            expectState(TodayViewData.Empty(userName = fixture.name()!!))
        }
    }

    @Test
    fun `should show empty with blank user name when user is not authenticated`() {
        every { mockAuthenticationRepository.currentUser() } returns null
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = ""))
        }
    }

    @Test
    fun `should show loaded when there are tasks for today`() {
        val task = Task.fixture()
        val taskViewData = FoliaryTaskCardViewData.fixture(id = task.id.toString())

        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockFoliaryTaskCardViewDataMapper(task) } returns taskViewData
        every { mockTaskRepository.findTodayTasks() } returns flowOf(listOf(task))

        test {
            expectState(TodayViewData.Loading)
            expectState(
                TodayViewData.Loaded(
                    userName = "Messias",
                    tasks = listOf(taskViewData),
                )
            )
        }
    }

    @Test
    fun `should navigate to create task when add task is clicked`() {
        every { mockAuthenticationRepository.currentUser() } returns UserInfo.fixture()
        every { mockTaskRepository.findTodayTasks() } returns flowOf(emptyList())

        test {
            expectState(TodayViewData.Loading)
            expectState(TodayViewData.Empty(userName = "Messias"))

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
            expectState(TodayViewData.Empty(userName = "Messias"))

            viewModel.onEvent(TodayEvent.OnTaskClick(taskId))

            expectSideEffect(TodayAction.NavigateToTaskDetail(taskId))
        }
    }
}
