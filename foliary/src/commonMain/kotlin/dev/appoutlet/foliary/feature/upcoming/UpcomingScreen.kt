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
import androidx.compose.foundation.lazy.LazyListScope
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
import dev.appoutlet.foliary.core.ui.component.layout.FoliaryEmptyContent
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCard
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_empty
import foliary.foliary.generated.resources.upcoming_add_task_a11y
import foliary.foliary.generated.resources.upcoming_description
import foliary.foliary.generated.resources.upcoming_empty_button
import foliary.foliary.generated.resources.upcoming_empty_description
import foliary.foliary.generated.resources.upcoming_empty_title
import foliary.foliary.generated.resources.upcoming_group_no_date
import foliary.foliary.generated.resources.upcoming_group_overdue
import foliary.foliary.generated.resources.upcoming_group_today
import foliary.foliary.generated.resources.upcoming_group_tomorrow
import foliary.foliary.generated.resources.upcoming_title
import org.jetbrains.compose.resources.painterResource
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
            UpcomingViewData.Empty -> UpcomingEmpty(onEvent)
            is UpcomingViewData.Loaded -> UpcomingLoaded(viewData, onEvent)
            else -> {}
        }
    }
}


private fun LazyListScope.UpcomingEmpty(onEvent: (UpcomingEvent) -> Unit) = item {
    FoliaryEmptyContent(
        modifier = Modifier.animateItem().testTag("UpcomingScreen:Empty"),
        image = painterResource(Res.drawable.today_empty),
        title = stringResource(Res.string.upcoming_empty_title),
        description = stringResource(Res.string.upcoming_empty_description),
        actionLabel = stringResource(Res.string.upcoming_empty_button),
        onActionClick = { onEvent(UpcomingEvent.OnAddTaskClick) },
    )
}

private fun LazyListScope.UpcomingLoaded(
    viewData: UpcomingViewData.Loaded,
    onEvent: (UpcomingEvent) -> Unit
) {
    viewData.taskGroup.forEach { group ->
        stickyHeader {
            UpcomingGroupHeader(
                label = group.label,
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
    label: UpcomingViewData.GroupLabel,
    modifier: Modifier = Modifier
) {
    val labelText = when (label) {
        is UpcomingViewData.GroupLabel.Date -> label.displayText
        UpcomingViewData.GroupLabel.NoDate -> stringResource(Res.string.upcoming_group_no_date)
        UpcomingViewData.GroupLabel.Overdue -> stringResource(Res.string.upcoming_group_overdue)
        UpcomingViewData.GroupLabel.Today -> stringResource(Res.string.upcoming_group_today)
        UpcomingViewData.GroupLabel.Tomorrow -> stringResource(Res.string.upcoming_group_tomorrow)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("UpcomingScreen:GroupHeader"),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = labelText,
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

private fun onAction(action: UpcomingAction, navigator: Navigator) {
    when (action) {
        UpcomingAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey())
        is UpcomingAction.NavigateToTaskDetail -> navigator.navigate(TaskDetailNavKey(action.taskId))
    }
}
