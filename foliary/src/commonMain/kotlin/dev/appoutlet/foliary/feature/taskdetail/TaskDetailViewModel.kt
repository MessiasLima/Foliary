package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ErrorState
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.task.TaskRepository
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_detail_not_found_message
import foliary.foliary.generated.resources.task_detail_not_found_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.getString
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
) : MviViewModel<TaskDetailViewData, TaskDetailAction>() {
    override val container = container(TaskDetailViewData.Idle)

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.LoadTask -> onLoadTask(event.taskId)
            TaskDetailEvent.BackClicked -> onBackClick()
            TaskDetailEvent.MarkCompletedClicked -> onMarkCompletedClick()
            TaskDetailEvent.DeleteClicked -> onDeleteClick()
        }
    }

    private fun onLoadTask(taskId: String) = intent {
        val id = runCatching { Uuid.parse(taskId) }.getOrNull()

        if (id == null) {
            onError(
                ErrorState(
                    error = IllegalArgumentException("Invalid task id: $taskId"),
                    title = getString(Res.string.task_detail_not_found_title),
                    message = getString(Res.string.task_detail_not_found_message),
                )
            )
            return@intent
        }

        reduce { TaskDetailViewData.Loading }

        taskRepository.findById(id)
            .collectLatest { task ->
                if (task == null) {
                    if (state is TaskDetailViewData.Loaded) {
                        postSideEffect(TaskDetailAction.NavigateBack)
                    } else {
                        onError(
                            ErrorState(
                                error = IllegalStateException("Task not found: $taskId"),
                                title = getString(Res.string.task_detail_not_found_title),
                                message = getString(Res.string.task_detail_not_found_message),
                            )
                        )
                    }
                    return@collectLatest
                }

                reduce {
                    TaskDetailViewData.Loaded(
                        task = TaskDetailViewData.Loaded.TaskViewData(task.title),
                    )
                }
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
    data class LoadTask(val taskId: String) : TaskDetailEvent
    data object BackClicked : TaskDetailEvent
    data object MarkCompletedClicked : TaskDetailEvent
    data object DeleteClicked : TaskDetailEvent
}

sealed interface TaskDetailAction : Action {
    data object NavigateBack : TaskDetailAction
    data object TaskMarkedCompleted : TaskDetailAction
}
