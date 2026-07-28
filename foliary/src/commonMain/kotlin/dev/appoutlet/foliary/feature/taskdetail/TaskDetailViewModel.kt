package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.task.TaskRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
class TaskDetailViewModel(
    @InjectedParam taskId: String,
    private val taskRepository: TaskRepository,
    private val taskViewDataMapper: TaskDataMapper,
) : MviViewModel<TaskDetailViewData, TaskDetailAction>() {
    private val id = Uuid.parse(taskId)

    override val container = container(TaskDetailViewData.Idle) {
        taskRepository.findById(id)
            .onStart { reduce { TaskDetailViewData.Loading } }
            .map { taskViewDataMapper(it) }
            .collect { reduce { TaskDetailViewData.Loaded(it) } }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            TaskDetailEvent.BackClicked -> onBackClick()
            TaskDetailEvent.MarkCompletedClicked -> onMarkCompletedClick()
            TaskDetailEvent.DeleteClicked -> onDeleteClick()
        }
    }

    private fun onBackClick() = intent {
        postSideEffect(TaskDetailAction.NavigateBack)
    }

    private fun onMarkCompletedClick() = intent {
        postSideEffect(TaskDetailAction.NavigateBack)
    }

    private fun onDeleteClick() = intent {
        postSideEffect(TaskDetailAction.NavigateBack)
    }
}

sealed interface TaskDetailViewData {
    data object Idle : TaskDetailViewData

    data object Loading : TaskDetailViewData

    data class Loaded(val task: TaskViewData) : TaskDetailViewData {
        data class TaskViewData(
            val title: String,
        )
    }
}

sealed interface TaskDetailEvent {
    data object BackClicked : TaskDetailEvent
    data object MarkCompletedClicked : TaskDetailEvent
    data object DeleteClicked : TaskDetailEvent
}

sealed interface TaskDetailAction : Action {
    data object NavigateBack : TaskDetailAction
}
