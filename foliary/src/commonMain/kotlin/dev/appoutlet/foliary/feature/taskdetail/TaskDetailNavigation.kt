package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Single

@OptIn(ExperimentalMaterial3Api::class)
@Single
class TaskDetailNavigation : Navigation<TaskDetailNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<TaskDetailNavKey> { navKey ->
            val viewModel = koinViewModel<TaskDetailViewModel>()

            Screen(
                screenName = "TaskDetailScreen",
                viewModelProvider = { viewModel },
                onAction = { action, navigator -> onAction(action, navigator) }
            ) { viewData: TaskDetailViewData ->
                TaskDetailScreen(
                    taskId = navKey.taskId,
                    viewData = viewData,
                    onEvent = viewModel::onEvent,
                )
            }
        }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(TaskDetailNavKey::class, TaskDetailNavKey.serializer())
    }

    private fun onAction(action: TaskDetailAction, navigator: Navigator) {
        when (action) {
            TaskDetailAction.NavigateBack -> navigator.goBack()
            TaskDetailAction.TaskMarkedCompleted -> Unit
        }
    }
}

@Serializable
data class TaskDetailNavKey(val taskId: String) : NavKey
