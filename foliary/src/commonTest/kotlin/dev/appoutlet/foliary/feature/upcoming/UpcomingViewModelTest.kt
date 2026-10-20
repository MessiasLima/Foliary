package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test

class UpcomingViewModelTest : ViewModelTest<UpcomingViewModel, UpcomingViewData, UpcomingAction>() {
    private val mockTaskRepository = mock<TaskRepository>()
    private val mockUpcomingGroupMapper = mock<UpcomingGroupMapper>()

    override fun createViewModel() = UpcomingViewModel(
        taskRepository = mockTaskRepository,
        upcomingGroupMapper = mockUpcomingGroupMapper
    )

    @Test
    fun `should emit Loading then Empty when there are no pending tasks`() {
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())
        every { mockUpcomingGroupMapper(emptyList()) } returns emptyList()

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)
        }
    }

    @Test
    fun `should emit Loading then Loaded with mapped groups when pending tasks exist`() {
        val tasks = listOf(Task.fixture())
        val groups = listOf(UpcomingGroup.fixture())

        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(tasks)
        every { mockUpcomingGroupMapper(tasks) } returns groups

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Loaded(groups))
        }
    }

    @Test
    fun `should emit NavigateToCreateTask side effect when add button is clicked`() {
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())
        every { mockUpcomingGroupMapper(emptyList()) } returns emptyList()

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)

            viewModel.onEvent(UpcomingEvent.OnAddTaskClick)

            expectSideEffect(UpcomingAction.NavigateToCreateTask)
        }
    }

    @Test
    fun `should emit NavigateToTaskDetail side effect when task is clicked`() {
        val taskId = "task-123"

        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())
        every { mockUpcomingGroupMapper(emptyList()) } returns emptyList()

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)

            viewModel.onEvent(UpcomingEvent.OnTaskClick(taskId))

            expectSideEffect(UpcomingAction.NavigateToTaskDetail(taskId))
        }
    }
}
