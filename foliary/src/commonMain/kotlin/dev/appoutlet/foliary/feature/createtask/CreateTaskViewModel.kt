package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.time.TimeProvider
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
class CreateTaskViewModel(
    private val taskRepository: TaskRepository,
    private val timeProvider: TimeProvider,
) : MviViewModel<CreateTaskViewData, CreateTaskAction>() {
    override val container = container(CreateTaskViewData())

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.OnTitleChanged -> onTitleChanged(event.title)
            is CreateTaskEvent.OnDescriptionChanged -> onDescriptionChanged(event.description)
            CreateTaskEvent.OnSaveClick -> onSaveClick()
            CreateTaskEvent.OnBackClick -> onBackClick()
        }
    }

    private fun onTitleChanged(title: String) = intent {
        reduce {
            state.copy(
                title = title,
                showTitleError = title.isBlank() && state.showTitleError
            )
        }
    }

    private fun onDescriptionChanged(description: String) = intent {
        reduce { state.copy(description = description) }
    }

    private fun onSaveClick() = intent {
        val trimmedTitle = state.title.trim()

        if (trimmedTitle.isBlank()) {
            reduce { state.copy(showTitleError = true) }
            return@intent
        }

        reduce { state.copy(isSaving = true) }

        val task = Task(
            id = Uuid.random(),
            title = trimmedTitle,
            description = state.description.trim().ifBlank { null },
            creationDate = timeProvider.now(),
            dueDate = null,
            completionDate = null,
            priority = null,
            url = null,
            location = null,
        )

        taskRepository.save(task)
        postSideEffect(CreateTaskAction.NavigateBack)
    }

    private fun onBackClick() = intent {
        postSideEffect(CreateTaskAction.NavigateBack)
    }
}

data class CreateTaskViewData(
    val title: String = "",
    val description: String = "",
    val showTitleError: Boolean = false,
    val isSaving: Boolean = false,
)

sealed interface CreateTaskEvent {
    data class OnTitleChanged(val title: String) : CreateTaskEvent
    data class OnDescriptionChanged(val description: String) : CreateTaskEvent
    data object OnSaveClick : CreateTaskEvent
    data object OnBackClick : CreateTaskEvent
}

sealed interface CreateTaskAction : Action {
    data object NavigateBack : CreateTaskAction
}
