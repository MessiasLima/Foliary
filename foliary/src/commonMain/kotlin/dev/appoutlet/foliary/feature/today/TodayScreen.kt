package dev.appoutlet.foliary.feature.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.FoliaryShadowColorDefault
import dev.appoutlet.foliary.core.ui.component.modifier.foliaryShadow
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCard
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_add_task_a11y
import foliary.foliary.generated.resources.today_celebration
import foliary.foliary.generated.resources.today_celebration_button
import foliary.foliary.generated.resources.today_celebration_description
import foliary.foliary.generated.resources.today_celebration_title
import foliary.foliary.generated.resources.today_completed_header
import foliary.foliary.generated.resources.today_completed_header_a11y
import foliary.foliary.generated.resources.today_empty
import foliary.foliary.generated.resources.today_empty_button
import foliary.foliary.generated.resources.today_empty_description
import foliary.foliary.generated.resources.today_empty_title
import foliary.foliary.generated.resources.today_title
import foliary.foliary.generated.resources.today_welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodayScreen(lazyListState: LazyListState) {
    val viewModel = koinViewModel<TodayViewModel>()

    Screen(
        screenName = "TodayScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) { viewData ->
        when (viewData) {
            TodayViewData.Idle -> {}
            TodayViewData.Loading -> LoadingIndicator()
            is TodayViewData.Loaded,
            is TodayViewData.Empty,
            is TodayViewData.Celebration -> TodayScreenContent(
                lazyListState = lazyListState,
                viewData = viewData,
                onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
internal fun TodayScreenContent(
    lazyListState: LazyListState,
    viewData: TodayViewData,
    onEvent: (TodayEvent) -> Unit
) {
    val userName = viewData.userName
    val completedTasks = viewData.completedTasks

    var completedCollapsed by remember { mutableStateOf(true) }
    val showActionShadow by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 1 }
    }

    LazyColumn(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .fillMaxSize(),
        state = lazyListState
    ) {
        stickyHeader { TodayAddButton(onEvent, showActionShadow) }
        item { } // Required for better UX
        item { TodayHeader(userName) }

        when (viewData) {
            is TodayViewData.Empty -> item {
                TodayScreenEmptyContent(
                    onEvent = onEvent,
                    modifier = if (completedTasks.isEmpty()) {
                        Modifier.animateItem().fillParentMaxSize()
                    } else {
                        Modifier.animateItem()
                    }
                )
            }

            is TodayViewData.Celebration -> item {
                TodayScreenCelebrationContent(
                    onEvent = onEvent,
                    modifier = Modifier.animateItem()
                )
            }

            is TodayViewData.Loaded -> {
                items(viewData.pendingTasks, key = { it.id }) { task ->
                    TodayTaskItem(
                        task = task,
                        onEvent = onEvent,
                        modifier = Modifier.animateItem()
                    )
                }
            }

            else -> {}
        }

        if (completedTasks.isNotEmpty()) {
            item(key = "CompletedHeader") {
                CompletedTodayHeader(
                    modifier = Modifier.animateItem(),
                    count = completedTasks.size,
                    collapsed = completedCollapsed,
                    onToggle = { completedCollapsed = !completedCollapsed }
                )
            }

            if (!completedCollapsed) {
                items(completedTasks, key = { it.id }) { task ->
                    TodayTaskItem(
                        task = task,
                        onEvent = onEvent,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayTaskItem(
    task: FoliaryTaskCardViewData,
    onEvent: (TodayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("TodayScreen:TaskItem")
    ) {
        FoliaryTaskCard(
            modifier = Modifier.widthInCompact()
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .align(Alignment.Center),
            task = task,
            onCompletedChange = { isCompleted ->
                if (isCompleted) {
                    onEvent(TodayEvent.MarkTaskAsCompleted(task.id))
                } else {
                    onEvent(TodayEvent.MarkTaskAsNotCompleted(task.id))
                }
            },
            onClick = { onEvent(TodayEvent.OnTaskClick(task.id)) }
        )
    }
}

@Composable
private fun CompletedTodayHeader(
    count: Int,
    collapsed: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (collapsed) RotationCollapsed else RotationExpanded
    )

    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            modifier = Modifier
                .widthInCompact()
                .fillMaxWidth()
                .clickable(onClick = onToggle, onClickLabel = stringResource(Res.string.today_completed_header_a11y))
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .testTag("TodayScreen:CompletedHeader"),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(Res.string.today_completed_header, count),
                style = MaterialTheme.typography.labelMedium
            )

            Icon(
                modifier = Modifier.rotate(chevronRotation),
                imageVector = Lucide.ChevronDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

}

@Composable
private fun TodayAddButton(
    onEvent: (TodayEvent) -> Unit,
    showActionShadow: Boolean,
) {
    val iconButtonShadowColor by animateColorAsState(
        targetValue = if (showActionShadow) FoliaryShadowColorDefault else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val iconButtonBorderColor by animateColorAsState(
        targetValue = if (showActionShadow) MaterialTheme.colorScheme.outline else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Row(
        modifier = Modifier.fillMaxWidth()
            .safeDrawingPadding()
            .padding(end = 8.dp, top = getWindowDecorationPadding()),
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedIconButton(
            modifier = Modifier.foliaryShadow(color = iconButtonShadowColor),
            onClick = { onEvent(TodayEvent.OnAddTaskClick) },
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(width = 1.dp, color = iconButtonBorderColor)
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = stringResource(Res.string.today_add_task_a11y)
            )
        }
    }
}

@Composable
private fun TodayHeader(userName: String, modifier: Modifier = Modifier) {
    Column(modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth()) {
        Text(
            modifier = Modifier,
            text = stringResource(Res.string.today_title),
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            modifier = Modifier,
            text = stringResource(Res.string.today_welcome, userName),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun TodayScreenEmptyContent(
    onEvent: (TodayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthInCompact()
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.height(192.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.today_empty),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            text = stringResource(Res.string.today_empty_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(Res.string.today_empty_description),
            style = MaterialTheme.typography.bodyMedium
        )

        FoliarySecondaryButton(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp),
            onClick = { onEvent(TodayEvent.OnAddTaskClick) },
        ) {
            Text(text = stringResource(Res.string.today_empty_button))
        }
    }
}

@Composable
private fun TodayScreenCelebrationContent(
    onEvent: (TodayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthInCompact()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.height(192.dp).align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.today_celebration),
            contentDescription = null
        )

        Text(
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp),
            text = stringResource(Res.string.today_celebration_title),
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = stringResource(Res.string.today_celebration_description),
            style = MaterialTheme.typography.bodyMedium
        )

        FoliarySecondaryButton(
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp),
            onClick = { onEvent(TodayEvent.OnAddTaskClick) },
        ) {
            Text(text = stringResource(Res.string.today_celebration_button))
        }
    }
}

private fun onAction(action: TodayAction, navigator: Navigator) {
    when (action) {
        TodayAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey())
        TodayAction.NavigateToSignIn -> navigator.setRoot(SignInNavKey)
        is TodayAction.NavigateToTaskDetail -> navigator.navigate(TaskDetailNavKey(action.taskId))
    }
}

private const val RotationCollapsed = -90f
private const val RotationExpanded = 0f
