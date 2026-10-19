package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.task.TaskRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class UpcomingViewModel(
    private val taskRepository: TaskRepository,
    private val upcomingGroupMapper: UpcomingGroupMapper
) : MviViewModel<UpcomingViewData, UpcomingAction>() {

    override val container = container(UpcomingViewData.Idle) {
        taskRepository.findAllPendingTasks()
            .onStart { reduce { UpcomingViewData.Loading } }
            .map { tasks -> upcomingGroupMapper(tasks) }
            .collect { groups ->
                reduce {
                    if (groups.isEmpty()) {
                        UpcomingViewData.Empty
                    } else {
                        UpcomingViewData.Loaded(groups)
                    }
                }
            }
    }

    fun onEvent(event: UpcomingEvent) {
        when (event) {
            is UpcomingEvent.OnTaskClick -> onTaskClick(event.taskId)
        }
    }

    private fun onTaskClick(taskId: String) = intent {
        postSideEffect(UpcomingAction.NavigateToTaskDetail(taskId))
    }
}

sealed interface UpcomingViewData {
    data object Idle : UpcomingViewData
    data object Loading : UpcomingViewData
    data class Loaded(val groups: List<UpcomingGroup>) : UpcomingViewData
    data object Empty : UpcomingViewData
}

sealed interface UpcomingEvent {
    data class OnTaskClick(val taskId: String) : UpcomingEvent
}

sealed interface UpcomingAction : Action {
    data class NavigateToTaskDetail(val taskId: String) : UpcomingAction
}
