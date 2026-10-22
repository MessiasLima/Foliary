package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.browser.BrowserLauncher
import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.data.task.TaskRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
class UpcomingViewModel(
    private val taskRepository: TaskRepository,
    private val upcomingViewDataMapper: UpcomingViewDataMapper,
    private val browserLauncher: BrowserLauncher,
) : MviViewModel<UpcomingViewData, UpcomingAction>() {

    override val container = container(UpcomingViewData.Idle) {
        taskRepository.findAllPendingTasks()
            .onStart { reduce { UpcomingViewData.Loading } }
            .map { tasks -> upcomingViewDataMapper(tasks) }
            .collect { viewData -> reduce { viewData } }
    }

    fun onEvent(event: UpcomingEvent) {
        when (event) {
            UpcomingEvent.OnAddTaskClick -> onAddTaskClick()
            is UpcomingEvent.OnTaskClick -> onTaskClick(event.taskId)
            is UpcomingEvent.OnUrlPillClick -> onUrlPillClick(event.taskId)
            is UpcomingEvent.MarkTaskAsCompleted -> onMarkTaskAsCompleted(event.taskId)
        }
    }

    private fun onMarkTaskAsCompleted(taskId: String) = intent {
        taskRepository.markCompleted(Uuid.parse(taskId))
    }

    private fun onAddTaskClick() = intent {
        postSideEffect(UpcomingAction.NavigateToCreateTask)
    }

    private fun onTaskClick(taskId: String) = intent {
        postSideEffect(UpcomingAction.NavigateToTaskDetail(taskId))
    }

    private fun onUrlPillClick(taskId: String) = intent {
        val task = taskRepository.getById(Uuid.parse(taskId))
        task.url?.let { browserLauncher.launch(it) }
    }
}

sealed interface UpcomingViewData {
    data object Idle : UpcomingViewData
    data object Loading : UpcomingViewData
    data class Loaded(val taskGroup: List<TaskGroup>) : UpcomingViewData
    data object Empty : UpcomingViewData

    sealed interface GroupLabel {
        data object Overdue : GroupLabel
        data object Today : GroupLabel
        data object Tomorrow : GroupLabel
        data class Date(val displayText: String) : GroupLabel
        data object NoDate : GroupLabel
    }

    data class TaskGroup(
        val label: GroupLabel,
        val tasks: List<FoliaryTaskCardViewData>
    ) {
        companion object
    }
}

sealed interface UpcomingEvent {
    data object OnAddTaskClick : UpcomingEvent
    data class OnTaskClick(val taskId: String) : UpcomingEvent
    data class OnUrlPillClick(val taskId: String) : UpcomingEvent
    data class MarkTaskAsCompleted(val taskId: String) : UpcomingEvent
}

sealed interface UpcomingAction : Action {
    data object NavigateToCreateTask : UpcomingAction
    data class NavigateToTaskDetail(val taskId: String) : UpcomingAction
}
