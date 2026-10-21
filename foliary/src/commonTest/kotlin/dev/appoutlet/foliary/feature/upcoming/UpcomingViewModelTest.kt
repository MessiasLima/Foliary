package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.browser.BrowserLauncher
import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlin.test.Test
import kotlin.uuid.Uuid

class UpcomingViewModelTest : ViewModelTest<UpcomingViewModel, UpcomingViewData, UpcomingAction>() {
    private val mockTaskRepository = mock<TaskRepository>()
    private val fakeUpcomingViewDataMapper = FakeUpcomingViewDataMapper()
    private val mockBrowserLauncher = mock<BrowserLauncher>(mode = MockMode.autoUnit)

    override fun createViewModel() = UpcomingViewModel(
        taskRepository = mockTaskRepository,
        upcomingViewDataMapper = fakeUpcomingViewDataMapper,
        browserLauncher = mockBrowserLauncher,
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

    @Test
    fun `should launch url in browser when url pill is clicked`() {
        val taskId = Uuid.random()
        val url = "https://example.com/page?q=1"

        fakeUpcomingViewDataMapper.result = UpcomingViewData.Empty
        every { mockTaskRepository.findAllPendingTasks() } returns flowOf(emptyList())
        everySuspend { mockTaskRepository.findById(taskId) } returns Task.fixture(id = taskId, url = url)

        test {
            expectState(UpcomingViewData.Loading)
            expectState(UpcomingViewData.Empty)

            viewModel.onEvent(UpcomingEvent.OnUrlPillClick(taskId.toString()))
            // Let the Orbit intent run before verifying its effect.
            delay(1)

            verify { mockBrowserLauncher.launch(url) }
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
