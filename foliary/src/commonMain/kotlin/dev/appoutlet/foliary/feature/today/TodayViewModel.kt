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

    override val container = container(TodayViewData.Idle) {
        taskRepository.findTodayTasks()
            .onStart { reduce { TodayViewData.Loading } }
            .map { tasks -> tasks.map { foliaryTaskCardViewDataMapper(it) } }
            .collect { tasks ->
                reduce {
                    if (tasks.isEmpty()) {
                        TodayViewData.Empty(userName = userName)
                    } else {
                        TodayViewData.Loaded(userName = userName, tasks = tasks)
                    }
                }
            }
    }

    fun onEvent(event: TodayEvent) {
        when (event) {
            TodayEvent.OnAddTaskClick -> onAddTaskClick()
        }
    }

    private fun onAddTaskClick() = intent {
        postSideEffect(TodayAction.NavigateToCreateTask)
    }
}

sealed interface TodayViewData {
    data object Idle : TodayViewData

    data object Loading : TodayViewData

    data class Loaded(
        val userName: String,
        val tasks: List<FoliaryTaskCardViewData>,
    ) : TodayViewData

    data class Empty(val userName: String) : TodayViewData
}

sealed interface TodayAction : Action {
    data object NavigateToCreateTask : TodayAction
    data object NavigateToSignIn : TodayAction
}

sealed interface TodayEvent {
    data object OnAddTaskClick : TodayEvent
}
