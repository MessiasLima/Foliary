package dev.appoutlet.foliary.feature.today

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.core.ui.component.modifier.FoliaryShadowColorDefault
import dev.appoutlet.foliary.core.ui.component.modifier.foliaryShadow
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCard
import dev.appoutlet.foliary.feature.createtask.CreateTaskNavKey
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_add_task_a11y
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
fun TodayScreen() {
    val viewModel = koinViewModel<TodayViewModel>()

    Screen(
        screenName = "TodayScreen",
        viewModelProvider = { viewModel },
        onAction = ::onAction
    ) { viewData ->
        when (viewData) {
            TodayViewData.Idle -> {}
            is TodayViewData.Loaded -> TodayScreenContent(viewData, viewModel::onEvent)
            TodayViewData.Loading -> LoadingIndicator()
            is TodayViewData.Empty -> TodayScreenEmpty(viewData, viewModel::onEvent)
        }
    }
}

@Composable
private fun TodayScreenContent(viewData: TodayViewData.Loaded, onEvent: (TodayEvent) -> Unit) {
    val lazyListState = rememberLazyListState()

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
        item { TodayHeader(viewData.userName) }
        items(viewData.tasks, key = { it.id }) { task ->
            Box(modifier = Modifier.fillMaxWidth()) {
                FoliaryTaskCard(
                    modifier = Modifier.widthInCompact()
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .align(Alignment.Center),
                    task = task,
                )
            }
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

private fun onAction(action: TodayAction, navigator: Navigator) {
    when (action) {
        TodayAction.NavigateToCreateTask -> navigator.navigate(CreateTaskNavKey)
        TodayAction.NavigateToSignIn -> navigator.setRoot(SignInNavKey)
    }
}

@Composable
private fun TodayScreenEmpty(viewData: TodayViewData.Empty, onEvent: (TodayEvent) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        TodayAddButton(onEvent = onEvent, showActionShadow = false)
        TodayHeader(modifier = Modifier.padding(top = 16.dp), userName = viewData.userName)
        Column(
            modifier = Modifier.widthInCompact()
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
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
}
