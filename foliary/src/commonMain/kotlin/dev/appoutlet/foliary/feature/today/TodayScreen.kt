package dev.appoutlet.foliary.feature.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.FoliaryShadowColorDefault
import dev.appoutlet.foliary.core.ui.component.modifier.foliaryShadow
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.task.TaskItem
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_add_task_a11y
import foliary.foliary.generated.resources.today_title
import foliary.foliary.generated.resources.today_welcome
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private val log = getLogger("TodayScreen")

@Composable
fun TodayScreen() {
    val viewModel = koinViewModel<TodayViewModel>()

    Screen(
        screenName = "TodayScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) { viewData ->
        TodayScreenContent(viewData, viewModel::onEvent)
    }
}

@Composable
private fun TodayScreenContent(viewData: TodayViewData, onEvent: (TodayEvent) -> Unit) {
    val lazyListState = rememberLazyListState()

    val showActionShadow by remember {
        derivedStateOf { lazyListState.firstVisibleItemIndex > 1 }
    }

    val iconButtonShadowColor by animateColorAsState(
        targetValue = if (showActionShadow) FoliaryShadowColorDefault else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val iconButtonBorderColor by animateColorAsState(
        targetValue = if (showActionShadow) MaterialTheme.colorScheme.outline else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    LazyColumn(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .fillMaxSize(),
        state = lazyListState
    ) {
        stickyHeader {
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

        // Required for better UX
        item {  }

        item {
            Column(Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp).fillMaxWidth()) {
                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.today_title),
                    style = MaterialTheme.typography.displaySmall
                )

                Text(
                    modifier = Modifier,
                    text = stringResource(Res.string.today_welcome, viewData.userName),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        repeat(20) {
            item {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TaskItem(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .widthInCompact()
                            .fillMaxWidth(),
                        title = "Task $it",
                    )
                }
            }
        }
    }
}

private fun onAction(action: TodayAction, navigator: Navigator) {
    when (action) {
        TodayAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey)
    }
}
