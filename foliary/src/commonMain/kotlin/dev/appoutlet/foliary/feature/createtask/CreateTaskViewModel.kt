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
            is CreateTaskEvent.TitleChanged -> onTitleChange(event.title)
            is CreateTaskEvent.DescriptionChanged -> onDescriptionChange(event.description)
            CreateTaskEvent.SaveClicked -> onSaveClick()
            CreateTaskEvent.BackClicked -> onBackClick()
        }
    }

    private fun onTitleChange(title: String) = intent {
        reduce {
            state.copy(
                title = title,
                saveButtonEnabled = title.isNotBlank()
            )
        }
    }

    private fun onDescriptionChange(description: String?) = intent {
        reduce { state.copy(description = description) }
    }

    private fun onSaveClick() = intent {
        reduce { state.copy(saveButtonEnabled = false) }

        val task = Task(
            id = state.id ?: Uuid.random(),
            title = state.title.trim(),
            description = state.description?.trim(),
            creationDate = timeProvider.now(),
            dueDate = null,
            completionDate = null,
            priority = null,
            url = null,
            location = null,
        )

        taskRepository.save(task)

        reduce { state.copy(saveButtonEnabled = true) }

        postSideEffect(CreateTaskAction.NavigateBack)
    }

    private fun onBackClick() = intent {
        postSideEffect(CreateTaskAction.NavigateBack)
    }
}

data class CreateTaskViewData(
    val id: Uuid? = null,
    val title: String = "",
    val description: String? = null,
    val saveButtonEnabled: Boolean = false,
)

sealed interface CreateTaskEvent {
    data class TitleChanged(val title: String) : CreateTaskEvent
    data class DescriptionChanged(val description: String?) : CreateTaskEvent
    data object SaveClicked : CreateTaskEvent
    data object BackClicked : CreateTaskEvent
}

sealed interface CreateTaskAction : Action {
    data object NavigateBack : CreateTaskAction
}
