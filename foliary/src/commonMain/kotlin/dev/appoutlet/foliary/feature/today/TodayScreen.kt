package dev.appoutlet.foliary.feature.today

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_add_task_content_description
import foliary.foliary.generated.resources.today_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodayScreen() {
    val viewModel = koinViewModel<TodayViewModel>()

    Screen(
        screenName = "TodayScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = stringResource(Res.string.today_title)) },
                    actions = {
                        IconButton(
                            onClick = { viewModel.onEvent(TodayEvent.OnAddTaskClick) }
                        ) {
                            Icon(
                                imageVector = Lucide.Plus,
                                contentDescription = stringResource(Res.string.today_add_task_content_description)
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(Res.string.today_title))
            }
        }
    }
}

private fun onAction(action: TodayAction, navigator: Navigator) {
    when (action) {
        TodayAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey)
    }
}
