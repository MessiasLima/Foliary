package dev.appoutlet.foliary.feature.createtask

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dev.appoutlet.foliary.core.navigation.Navigation
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.Single

@Single
class CreateTaskNavigation : Navigation<CreateTaskNavKey> {
    override fun setupRoute(scope: EntryProviderScope<NavKey>) {
        scope.entry<CreateTaskNavKey> {
            val viewModel = koinViewModel<CreateTaskViewModel>()
            Screen(
                screenName = "CreateTaskScreen",
                viewModelProvider = { viewModel },
                onAction = ::onAction
            ) { viewData: CreateTaskViewData ->
                CreateTaskScreen(viewData, viewModel::onEvent)
            }
        }
    }

    override fun setupPolymorphism(builder: PolymorphicModuleBuilder<NavKey>) {
        builder.subclass(CreateTaskNavKey::class, CreateTaskNavKey.serializer())
    }

    private fun onAction(action: CreateTaskAction, navigator: Navigator) {
        when (action) {
            CreateTaskAction.NavigateBack -> navigator.goBack()
        }
    }
}

@Serializable
data object CreateTaskNavKey : NavKey
