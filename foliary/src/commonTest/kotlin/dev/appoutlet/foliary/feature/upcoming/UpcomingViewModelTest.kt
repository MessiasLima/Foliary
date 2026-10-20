package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test

class UpcomingViewModelTest : ViewModelTest<UpcomingViewModel, UpcomingViewData, UpcomingAction>() {
    private val mockTaskRepository = mock<TaskRepository>()
    private val fakeUpcomingViewDataMapper = FakeUpcomingViewDataMapper()

    override fun createViewModel() = UpcomingViewModel(
        taskRepository = mockTaskRepository,
        upcomingViewDataMapper = fakeUpcomingViewDataMapper
    )

    @Test
    fun `should emit Loading then Empty when there are no pending tasks`() {
        fakeUpcomingViewDataMapper.result = UpcomingViewData.Empty
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)
            fakeUpcomingViewDataMapper.lastTasks shouldBe emptyList()
        }
    }

    @Test
    fun `should emit Loading then Loaded with mapped groups when pending tasks exist`() {
        val tasks = listOf(Task.fixture())
        val loadedViewData = UpcomingViewData.Loaded(
            listOf(UpcomingViewData.TaskGroup.fixture())
        )

        fakeUpcomingViewDataMapper.result = loadedViewData
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(tasks)

        test {
            expectState(UpcomingViewData.Loading)
            expectState(loadedViewData)
            fakeUpcomingViewDataMapper.lastTasks shouldBe tasks
        }
    }

    @Test
    fun `should emit NavigateToCreateTask side effect when add button is clicked`() {
        fakeUpcomingViewDataMapper.result = UpcomingViewData.Empty
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())

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

        fakeUpcomingViewDataMapper.result = UpcomingViewData.Empty
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)

            viewModel.onEvent(UpcomingEvent.OnTaskClick(taskId))

            expectSideEffect(UpcomingAction.NavigateToTaskDetail(taskId))
        }
    }

    private class FakeUpcomingViewDataMapper(
        timeProvider: DefaultTimeProvider = DefaultTimeProvider(),
    ) : UpcomingViewDataMapper(
        timeProvider = timeProvider,
        foliaryTaskCardViewDataMapper = FoliaryTaskCardViewDataMapper(timeProvider),
    ) {
        var result: UpcomingViewData = UpcomingViewData.Empty
        var lastTasks: List<Task>? = null

        override fun invoke(tasks: List<Task>): UpcomingViewData {
            lastTasks = tasks
            return result
        }
    }
}
