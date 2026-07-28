package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.ui.component.button.FoliaryBackIconButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_detail_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskDetailScreen(
    taskId: String,
    viewData: TaskDetailViewData,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    LaunchedEffect(taskId) {
        onEvent(TaskDetailEvent.LoadTask(taskId))
    }

    Scaffold(topBar = { TaskDetailTopBar(onEvent) }) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            when (viewData) {
                TaskDetailViewData.Idle -> {}
                TaskDetailViewData.Loading -> LoadingIndicator()
                is TaskDetailViewData.Loaded -> TaskDetailContent(
                    viewData = viewData,
                    onEvent = onEvent,
                )
            }
        }
    }
}

@Composable
private fun TaskDetailTopBar(onEvent: (TaskDetailEvent) -> Unit) {
    TopAppBar(
        modifier = Modifier.padding(top = getWindowDecorationPadding()),
        navigationIcon = {
            FoliaryBackIconButton(onClick = { onEvent(TaskDetailEvent.BackClicked) })
        },
        title = {
            Text(
                text = stringResource(Res.string.task_detail_title),
                color = MaterialTheme.colorScheme.primary,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}

@Suppress("UNUSED")
@Composable
private fun TaskDetailContent(
    viewData: TaskDetailViewData.Loaded,
    onEvent: (TaskDetailEvent) -> Unit,
) {
}
