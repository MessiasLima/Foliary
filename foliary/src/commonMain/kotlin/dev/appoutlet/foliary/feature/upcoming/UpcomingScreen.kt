package dev.appoutlet.foliary.feature.upcoming

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliaryAddButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCard
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.upcoming_add_task_a11y
import foliary.foliary.generated.resources.upcoming_description
import foliary.foliary.generated.resources.upcoming_empty_description
import foliary.foliary.generated.resources.upcoming_empty_title
import foliary.foliary.generated.resources.upcoming_group_header
import foliary.foliary.generated.resources.upcoming_group_no_date
import foliary.foliary.generated.resources.upcoming_group_overdue
import foliary.foliary.generated.resources.upcoming_group_today
import foliary.foliary.generated.resources.upcoming_group_tomorrow
import foliary.foliary.generated.resources.upcoming_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UpcomingScreen(lazyListState: LazyListState) {
    val viewModel = koinViewModel<UpcomingViewModel>()

    Screen(
        screenName = "UpcomingScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) { viewData ->
        when (viewData) {
            UpcomingViewData.Idle -> {}
            UpcomingViewData.Loading -> LoadingIndicator()
            is UpcomingViewData.Loaded,
            UpcomingViewData.Empty -> UpcomingScreenContent(
                lazyListState = lazyListState,
                viewData = viewData,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
internal fun UpcomingScreenContent(
    lazyListState: LazyListState,
    viewData: UpcomingViewData,
    onEvent: (UpcomingEvent) -> Unit
) {
    val showActionShadow by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 1 }
    }

    LazyColumn(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .fillMaxSize(),
        state = lazyListState
    ) {
        stickyHeader {
            FoliaryAddButton(
                onClick = { onEvent(UpcomingEvent.OnAddTaskClick) },
                contentDescription = stringResource(Res.string.upcoming_add_task_a11y),
                modifier = Modifier.padding(top = getWindowDecorationPadding()),
                showShadow = showActionShadow
            )
        }
        item { } // Required for better UX
        item { UpcomingHeader() }

        when (viewData) {
            UpcomingViewData.Empty -> item {
                UpcomingScreenEmptyContent(modifier = Modifier.animateItem())
            }

            is UpcomingViewData.Loaded -> {
                for (group in viewData.groups) {
                    item(key = "Header_${group.label}") {
                        UpcomingGroupHeader(
                            label = group.label,
                            taskCount = group.tasks.size,
                            modifier = Modifier.animateItem()
                        )
                    }

                    items(group.tasks, key = { it.id }) { task ->
                        UpcomingTaskItem(
                            task = task,
                            onEvent = onEvent,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun UpcomingHeader(modifier: Modifier = Modifier) {
    Column(modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth()) {
        Text(
            modifier = Modifier,
            text = stringResource(Res.string.upcoming_title),
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            modifier = Modifier,
            text = stringResource(Res.string.upcoming_description),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun UpcomingGroupHeader(
    label: UpcomingGroupLabel,
    taskCount: Int,
    modifier: Modifier = Modifier
) {
    val labelText = when (label) {
        UpcomingGroupLabel.Overdue -> stringResource(Res.string.upcoming_group_overdue)
        UpcomingGroupLabel.Today -> stringResource(Res.string.upcoming_group_today)
        UpcomingGroupLabel.Tomorrow -> stringResource(Res.string.upcoming_group_tomorrow)
        is UpcomingGroupLabel.Date -> label.displayText
        UpcomingGroupLabel.NoDate -> stringResource(Res.string.upcoming_group_no_date)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("UpcomingScreen:GroupHeader"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(Res.string.upcoming_group_header, labelText, taskCount),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .widthInCompact()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun UpcomingTaskItem(
    task: FoliaryTaskCardViewData,
    onEvent: (UpcomingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("UpcomingScreen:TaskItem")
    ) {
        FoliaryTaskCard(
            modifier = Modifier
                .widthInCompact()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .align(Alignment.Center),
            task = task,
            onCompletedChange = {},
            onClick = { onEvent(UpcomingEvent.OnTaskClick(task.id)) }
        )
    }
}

@Composable
private fun UpcomingScreenEmptyContent(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthInCompact()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 56.dp)
            .testTag("UpcomingScreen:Empty"),
    ) {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = stringResource(Res.string.upcoming_empty_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(Res.string.upcoming_empty_description),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

private fun onAction(action: UpcomingAction, navigator: Navigator) {
    when (action) {
        UpcomingAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey())
        is UpcomingAction.NavigateToTaskDetail -> navigator.navigate(TaskDetailNavKey(action.taskId))
    }
}
