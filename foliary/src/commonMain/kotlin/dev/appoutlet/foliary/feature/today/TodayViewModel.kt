package dev.appoutlet.foliary.feature.today

import dev.appoutlet.foliary.core.mvi.Action
import dev.appoutlet.foliary.core.mvi.MviViewModel
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.authentication.AuthenticationRepository
import dev.appoutlet.foliary.data.authentication.util.name
import dev.appoutlet.foliary.data.task.TaskRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
class TodayViewModel(
    private val taskRepository: TaskRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val foliaryTaskCardViewDataMapper: FoliaryTaskCardViewDataMapper
) : MviViewModel<TodayViewData, TodayAction>() {
    private val currentUser by lazy {
        authenticationRepository.currentUser()
    }

    private val userName by lazy {
        if (currentUser == null) {
            log.w { "User is not authenticated" }
        }

        currentUser?.name() ?: ""
    }

    private var celebrationPending = false

    override val container = container(TodayViewData.Idle) {
        taskRepository.findTodayTasks()
            .onStart { reduce { TodayViewData.Loading } }
            .map { tasks -> tasks.map { foliaryTaskCardViewDataMapper(it) } }
            .map { tasks ->
                val pendingTasks = tasks.filter { !it.isCompleted }
                val completedTasks = tasks.filter { it.isCompleted }
                pendingTasks to completedTasks
            }
            .collect { (pendingTasks, completedTasks) ->
                reduce {
                    when {
                        celebrationPending && pendingTasks.isEmpty() -> {
                            celebrationPending = false
                            TodayViewData.Celebration(
                                userName = userName,
                                completedTasks = completedTasks
                            )
                        }
                        pendingTasks.isEmpty() -> {
                            celebrationPending = false
                            TodayViewData.Empty(
                                userName = userName,
                                completedTasks = completedTasks
                            )
                        }
                        else -> {
                            celebrationPending = false
                            TodayViewData.Loaded(
                                userName = userName,
                                pendingTasks = pendingTasks,
                                completedTasks = completedTasks
                            )
                        }
                    }
                }
            }
    }

    fun onEvent(event: TodayEvent) {
        when (event) {
            TodayEvent.OnAddTaskClick -> onAddTaskClick()
            is TodayEvent.OnTaskClick -> onTaskClick(event.taskId)
            is TodayEvent.MarkTaskAsCompleted -> onMarkTaskAsCompleted(event.taskId)
            is TodayEvent.MarkTaskAsNotCompleted -> onMarkTaskAsNotCompleted(event.taskId)
        }
    }

    private fun onAddTaskClick() = intent {
        postSideEffect(TodayAction.NavigateToCreateTask)
    }

    private fun onTaskClick(taskId: String) = intent {
        postSideEffect(TodayAction.NavigateToTaskDetail(taskId))
    }

    private fun onMarkTaskAsCompleted(taskId: String) = intent {
        celebrationPending = true
        taskRepository.markCompleted(Uuid.parse(taskId))
    }

    private fun onMarkTaskAsNotCompleted(taskId: String) = intent {
        celebrationPending = false
        taskRepository.markNotCompleted(Uuid.parse(taskId))
    }
}

sealed interface TodayViewData {
    val userName: String
    val completedTasks: List<FoliaryTaskCardViewData>

    data object Idle : TodayViewData {
        override val userName: String = ""
        override val completedTasks: List<FoliaryTaskCardViewData> = emptyList()
    }

    data object Loading : TodayViewData {
        override val userName: String = ""
        override val completedTasks: List<FoliaryTaskCardViewData> = emptyList()
    }

    data class Loaded(
        override val userName: String,
        val pendingTasks: List<FoliaryTaskCardViewData>,
        override val completedTasks: List<FoliaryTaskCardViewData>,
    ) : TodayViewData

    data class Empty(
        override val userName: String,
        override val completedTasks: List<FoliaryTaskCardViewData>,
    ) : TodayViewData

    data class Celebration(
        override val userName: String,
        override val completedTasks: List<FoliaryTaskCardViewData>,
    ) : TodayViewData
}

sealed interface TodayAction : Action {
    data object NavigateToCreateTask : TodayAction
    data object NavigateToSignIn : TodayAction
    data class NavigateToTaskDetail(val taskId: String) : TodayAction
}

sealed interface TodayEvent {
    data object OnAddTaskClick : TodayEvent
    data class OnTaskClick(val taskId: String) : TodayEvent
    data class MarkTaskAsCompleted(val taskId: String) : TodayEvent
    data class MarkTaskAsNotCompleted(val taskId: String) : TodayEvent
}
