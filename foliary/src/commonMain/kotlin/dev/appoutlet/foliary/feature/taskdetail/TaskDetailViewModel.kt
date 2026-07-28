package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.ErrorState
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_detail_not_found_message
import foliary.foliary.generated.resources.task_detail_not_found_title
import kotlinx.coroutines.flow.collectLatest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.getString
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

@KoinViewModel
class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val timeProvider: TimeProvider,
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
                        task = task,
                        isOverdue = mapIsOverdue(task),
                    )
                }
            }
    }

    private fun onBackClick() = intent {
        postSideEffect(TaskDetailAction.NavigateBack)
    }

    private fun onMarkCompletedClick() = intent {
        val loaded = state as? TaskDetailViewData.Loaded ?: return@intent
        if (loaded.task.completionDate != null) return@intent

        val updatedTask = loaded.task.copy(
            completionDate = loaded.task.completionDate ?: timeProvider.now()
        )

        taskRepository.save(updatedTask)
        postSideEffect(TaskDetailAction.TaskMarkedCompleted)
    }

    private fun onDeleteClick() = intent {
        val loaded = state as? TaskDetailViewData.Loaded ?: return@intent

        taskRepository.delete(loaded.task.id)
        postSideEffect(TaskDetailAction.NavigateBack)
    }

    private fun mapIsOverdue(task: Task): Boolean {
        if (task.completionDate != null) return false
        val dueDate = task.dueDate ?: return false
        return dueDate < timeProvider.now()
    }

    private fun formatInstant(instant: Instant): String {
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    }
}

sealed interface TaskDetailViewData {
    data object Idle : TaskDetailViewData
    data object Loading : TaskDetailViewData
    data class Loaded(
        val task: Task,
        val isOverdue: Boolean,
    ) : TaskDetailViewData
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
