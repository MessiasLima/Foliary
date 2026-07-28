package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
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
            .onEach { if (it == null) postSideEffect(TaskDetailAction.NavigateBack) }
            .filterNotNull()
            .map { taskViewDataMapper(it) }
            .collect { reduce { TaskDetailViewData.Loaded(it) } }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            TaskDetailEvent.BackClicked -> onBackClick()
            TaskDetailEvent.MarkCompletedClicked -> onMarkCompletedClick()
            TaskDetailEvent.DeleteClicked -> onDeleteClick()
            TaskDetailEvent.MarkNotCompletedClicked -> onMarkNotCompletedClick()
            TaskDetailEvent.EditClicked -> onEditClick()
        }
    }

    private fun onEditClick() = intent {
        // TODO edit
    }

    private fun onMarkNotCompletedClick() = intent {
        taskRepository.markNotCompleted(id)
    }

    private fun onBackClick() = intent {
        postSideEffect(TaskDetailAction.NavigateBack)
    }

    private fun onMarkCompletedClick() = intent {
        taskRepository.markCompleted(id)
    }

    private fun onDeleteClick() = intent {
        taskRepository.delete(id)
    }
}

sealed interface TaskDetailViewData {
    data object Idle : TaskDetailViewData

    data object Loading : TaskDetailViewData

    data class Loaded(val task: TaskViewData) : TaskDetailViewData {
        data class TaskViewData(
            val title: String,
            val description: String?,
            val isComplete: Boolean,
        )
    }
}

sealed interface TaskDetailEvent {
    data object BackClicked : TaskDetailEvent
    data object MarkCompletedClicked : TaskDetailEvent
    data object MarkNotCompletedClicked : TaskDetailEvent
    data object DeleteClicked : TaskDetailEvent
    data object EditClicked : TaskDetailEvent
}

sealed interface TaskDetailAction : Action {
    data object NavigateBack : TaskDetailAction
}
