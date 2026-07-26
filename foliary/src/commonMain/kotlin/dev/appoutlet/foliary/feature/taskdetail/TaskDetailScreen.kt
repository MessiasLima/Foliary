package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.button.FoliaryBackIconButton
import dev.appoutlet.foliary.core.ui.component.button.FoliaryOutlinedButton
import dev.appoutlet.foliary.core.ui.component.button.FoliaryPrimaryButton
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.feature.main.getWindowDecorationPadding
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_detail_completion_date_label
import foliary.foliary.generated.resources.task_detail_creation_date_label
import foliary.foliary.generated.resources.task_detail_delete
import foliary.foliary.generated.resources.task_detail_description_label
import foliary.foliary.generated.resources.task_detail_due_date_label
import foliary.foliary.generated.resources.task_detail_mark_completed
import foliary.foliary.generated.resources.task_detail_no_description
import foliary.foliary.generated.resources.task_detail_no_due_date
import foliary.foliary.generated.resources.task_detail_no_priority
import foliary.foliary.generated.resources.task_detail_priority_blocker
import foliary.foliary.generated.resources.task_detail_priority_high
import foliary.foliary.generated.resources.task_detail_priority_highest
import foliary.foliary.generated.resources.task_detail_priority_label
import foliary.foliary.generated.resources.task_detail_priority_low
import foliary.foliary.generated.resources.task_detail_priority_lowest
import foliary.foliary.generated.resources.task_detail_priority_medium
import foliary.foliary.generated.resources.task_detail_share
import foliary.foliary.generated.resources.task_detail_status_completed
import foliary.foliary.generated.resources.task_detail_status_overdue
import foliary.foliary.generated.resources.task_detail_task_title_label
import foliary.foliary.generated.resources.task_detail_title
import foliary.foliary.generated.resources.task_detail_url_label
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

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

@Composable
private fun TaskDetailContent(
    viewData: TaskDetailViewData.Loaded,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    val task = viewData.task
    val uriHandler = LocalUriHandler.current

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .widthInCompact()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            StatusIndicator(viewData)
            DetailSection(
                label = stringResource(Res.string.task_detail_task_title_label),
                value = task.title,
                valueStyle = MaterialTheme.typography.headlineSmall,
                testTag = "TaskDetailScreen:Title",
            )
            DetailSection(
                label = stringResource(Res.string.task_detail_description_label),
                value = task.description?.takeIf { it.isNotBlank() }
                    ?: stringResource(Res.string.task_detail_no_description),
                testTag = "TaskDetailScreen:Description",
            )
            DetailSection(
                label = stringResource(Res.string.task_detail_due_date_label),
                value = task.dueDate?.let { formatInstant(it) }
                    ?: stringResource(Res.string.task_detail_no_due_date),
                testTag = "TaskDetailScreen:DueDate",
            )
            DetailSection(
                label = stringResource(Res.string.task_detail_priority_label),
                value = task.priority?.let { stringResource(it.toStringResource()) }
                    ?: stringResource(Res.string.task_detail_no_priority),
                testTag = "TaskDetailScreen:Priority",
            )
            task.url?.takeIf { it.isNotBlank() }?.let { url ->
                UrlSection(
                    url = url,
                    onClick = { uriHandler.openUri(url) },
                )
            }
            DetailSection(
                label = stringResource(Res.string.task_detail_creation_date_label),
                value = formatInstant(task.creationDate),
                testTag = "TaskDetailScreen:CreationDate",
            )
            task.completionDate?.let { completionDate ->
                DetailSection(
                    label = stringResource(Res.string.task_detail_completion_date_label),
                    value = formatInstant(completionDate),
                    testTag = "TaskDetailScreen:CompletionDate",
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        TaskDetailActions(
            modifier = Modifier.align(Alignment.BottomCenter),
            isCompleted = task.completionDate != null,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun StatusIndicator(viewData: TaskDetailViewData.Loaded) {
    when {
        viewData.task.completionDate != null -> {
            StatusPill(
                text = stringResource(Res.string.task_detail_status_completed),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                testTag = "TaskDetailScreen:CompletedIndicator",
            )
        }

        viewData.isOverdue -> {
            StatusPill(
                text = stringResource(Res.string.task_detail_status_overdue),
                color = MaterialTheme.colorScheme.error,
                testTag = "TaskDetailScreen:OverdueIndicator",
            )
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    color: Color,
    testTag: String,
) {
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(color = color.copy(alpha = StatusPillBackgroundAlpha))
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color)
                .size(8.dp),
        )

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
        )
    }
}

@Composable
private fun DetailSection(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    testTag: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            modifier = Modifier.testTag(testTag),
            style = valueStyle,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun UrlSection(
    url: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(Res.string.task_detail_url_label),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        FoliarySecondaryButton(
            modifier = Modifier.fillMaxWidth().testTag("TaskDetailScreen:Url"),
            onClick = onClick,
        ) {
            Text(
                text = url,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun TaskDetailActions(
    modifier: Modifier = Modifier,
    isCompleted: Boolean,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    Column(
        modifier = modifier
            .widthInCompact()
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FoliaryPrimaryButton(
            modifier = Modifier.fillMaxWidth().testTag("TaskDetailScreen:MarkCompletedButton"),
            onClick = { onEvent(TaskDetailEvent.MarkCompletedClicked) },
            enabled = !isCompleted,
        ) {
            Text(text = stringResource(Res.string.task_detail_mark_completed))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FoliaryOutlinedButton(
                modifier = Modifier.weight(1f).testTag("TaskDetailScreen:DeleteButton"),
                onClick = { onEvent(TaskDetailEvent.DeleteClicked) },
            ) {
                Text(text = stringResource(Res.string.task_detail_delete))
            }

            FoliaryOutlinedButton(
                modifier = Modifier.weight(1f).testTag("TaskDetailScreen:ShareButton"),
                onClick = { onEvent(TaskDetailEvent.ShareClicked) },
            ) {
                Text(text = stringResource(Res.string.task_detail_share))
            }
        }
    }
}

@Composable
private fun Priority.toStringResource() = when (this) {
    Priority.LOWEST -> Res.string.task_detail_priority_lowest
    Priority.LOW -> Res.string.task_detail_priority_low
    Priority.MEDIUM -> Res.string.task_detail_priority_medium
    Priority.HIGH -> Res.string.task_detail_priority_high
    Priority.HIGHEST -> Res.string.task_detail_priority_highest
    Priority.BLOCKER -> Res.string.task_detail_priority_blocker
}

private fun formatInstant(instant: Instant): String {
    return instant.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
}

private const val StatusPillBackgroundAlpha = 0.12f
