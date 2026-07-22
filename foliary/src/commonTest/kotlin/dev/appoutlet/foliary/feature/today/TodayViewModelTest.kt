package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.testing.ViewModelTest
import kotlin.test.Test

class TodayViewModelTest : ViewModelTest<TodayViewModel, TodayViewData, TodayAction>() {
    override fun createViewModel() = TodayViewModel()

    @Test
    fun `should navigate to create task when add task is clicked`() = test {
        viewModel.onEvent(TodayEvent.OnAddTaskClick)

        expectSideEffect(TodayAction.NavigateToCreateTask)
    }
}
