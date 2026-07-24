package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.testing.ViewModelTest
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.mokkery.mock
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
    fun `should navigate to create task when add task is clicked`() = test {
        viewModel.onEvent(TodayEvent.OnAddTaskClick)

        expectSideEffect(TodayAction.NavigateToCreateTask)
    }
}
