package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.ui.component.appbar.FoliaryTopAppBar
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator

@Composable
fun TaskDetailScreen(
    taskId: String,
    viewData: TaskDetailViewData,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    AnimatedContent(targetState = viewData, contentKey = { it::class }) {
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

@Composable
private fun TaskDetailTopBar(onEvent: (TaskDetailEvent) -> Unit) {
    FoliaryTopAppBar(
        modifier = Modifier,
        navigationIcon = { },
        title = { },
    )
}

@Suppress("UNUSED", "EmptyFunctionBlock", "UnusedParameter")
@Composable
private fun TaskDetailContent(
    viewData: TaskDetailViewData.Loaded,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    Box(Modifier.fillMaxWidth()) {
        TaskDetailTopBar(onEvent = onEvent)
    }
}
