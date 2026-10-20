package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.provider.uuid.UuidProvider
import dev.appoutlet.foliary.data.task.TaskRepository
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Instant
import kotlin.uuid.Uuid

@KoinViewModel
class CreateTaskViewModel(
    @InjectedParam private val taskId: String?,
    private val taskRepository: TaskRepository,
    private val timeProvider: TimeProvider,
    private val uuidProvider: UuidProvider,
) : MviViewModel<CreateTaskViewData, CreateTaskAction>() {
    private val startOfToday = timeProvider.startOfToday().toEpochMilliseconds()
    private var savedTask: Task? = null

    override val container = container(CreateTaskViewData(minDueDateMillis = startOfToday)) {
        taskId?.also { id -> loadSavedTask(id) }
    }

    private fun loadSavedTask(id: String) = intent {
        val uuid = Uuid.parse(id)
        savedTask = taskRepository.findById(uuid) ?: return@intent
        savedTask?.let { fillSavedTask(it) }
    }

    private fun fillSavedTask(task: Task) = intent {
        reduce {
            CreateTaskViewData(
                id = task.id.toString(),
                title = task.title,
                description = task.description,
                url = task.url.orEmpty(),
                priority = task.priority,
                dueDate = task.dueDate?.let { dueDate ->
                    CreateTaskViewData.DueDateViewData(
                        selectedDateMillis = dueDate.toEpochMilliseconds(),
                        selectedDateDisplayText = timeProvider.displayText(dueDate)
                    )
                },
                minDueDateMillis = startOfToday,
                saveButtonEnabled = true
            )
        }
    }

    fun onEvent(event: CreateTaskEvent) {
        when (event) {
            is CreateTaskEvent.TitleChanged -> onTitleChange(event.title)
            is CreateTaskEvent.DescriptionChanged -> onDescriptionChange(event.description)
            is CreateTaskEvent.UrlChanged -> onUrlChange(event.url)
            is CreateTaskEvent.PriorityChanged -> onPriorityChange(event.priority)
            is CreateTaskEvent.DueDateChanged -> onDueDateChange(event.dueDateMillis)
            CreateTaskEvent.SaveClicked -> onSaveClick()
            CreateTaskEvent.BackClicked -> onBackClick()
        }
    }

    private fun onUrlChange(url: String) = intent {
        reduce { state.copy(url = url, isUrlInvalid = false) }
    }

    private fun onPriorityChange(priority: Priority) = intent {
        reduce { state.copy(priority = priority) }
    }

    private fun onDueDateChange(dueDateMillis: Long?) = intent {
        reduce {
            state.copy(
                dueDate = dueDateMillis?.let {
                    CreateTaskViewData.DueDateViewData(
                        selectedDateMillis = it,
                        selectedDateDisplayText = timeProvider.displayText(it.toInstant()),
                    )
                }
            )
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
        val url = state.url.toNormalizedUrl()
        if (url != null && !UrlRegex.matches(url)) {
            reduce { state.copy(isUrlInvalid = true) }
            return@intent
        }

        reduce { state.copy(saveButtonEnabled = false) }

        val task = Task(
            id = state.id?.let { Uuid.parse(it) } ?: uuidProvider.random(),
            title = state.title.trim(),
            description = state.description?.trim()?.let { description ->
                description.takeIf { it.isNotBlank() }
            },
            creationDate = savedTask?.creationDate ?: timeProvider.now(),
            dueDate = state.dueDate?.selectedDateMillis?.toInstant()?.let {
                timeProvider.endOfDay(it)
            },
            completionDate = null,
            priority = state.priority,
            url = url,
            location = null,
        )

        taskRepository.save(task)

        postSideEffect(CreateTaskAction.NavigateBack)
    }

    private fun onBackClick() = intent {
        postSideEffect(CreateTaskAction.NavigateBack)
    }

    private fun Long.toInstant() = Instant.fromEpochMilliseconds(this)
}

private val UrlRegex = Regex(
    "^https?://([a-z0-9-]+\\.)+[a-z]{2,}(:\\d{1,5})?(/\\S*)?$",
    RegexOption.IGNORE_CASE,
)

private fun String.toNormalizedUrl(): String? {
    val trimmed = trim()
    if (trimmed.isBlank()) return null
    return if (trimmed.contains("://")) {
        trimmed
    } else {
        "https://$trimmed"
    }
}

data class CreateTaskViewData(
    val id: String? = null,
    val title: String = "",
    val description: String? = null,
    val url: String = "",
    val isUrlInvalid: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: DueDateViewData? = null,
    val minDueDateMillis: Long,
    val saveButtonEnabled: Boolean = false,
) {
    data class DueDateViewData(
        val selectedDateMillis: Long,
        val selectedDateDisplayText: String,
    ) {
        companion object
    }

    companion object
}

sealed interface CreateTaskEvent {
    data class TitleChanged(val title: String) : CreateTaskEvent
    data class DescriptionChanged(val description: String?) : CreateTaskEvent
    data class UrlChanged(val url: String) : CreateTaskEvent
    data class PriorityChanged(val priority: Priority) : CreateTaskEvent
    data class DueDateChanged(val dueDateMillis: Long?) : CreateTaskEvent
    data object SaveClicked : CreateTaskEvent
    data object BackClicked : CreateTaskEvent
}

sealed interface CreateTaskAction : Action {
    data object NavigateBack : CreateTaskAction
}
