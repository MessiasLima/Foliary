package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CalendarCheck
import com.composables.icons.lucide.CalendarClock
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleCheckBig
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Pencil
import com.composables.icons.lucide.Trash
import dev.appoutlet.foliary.core.ui.component.button.FoliaryMenuIconButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import dev.appoutlet.foliary.core.ui.component.pill.OverduePill
import dev.appoutlet.foliary.core.ui.component.pill.PriorityPill
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.general_cancel
import foliary.foliary.generated.resources.task_detail_completion_date_label
import foliary.foliary.generated.resources.task_detail_creation_date_label
import foliary.foliary.generated.resources.task_detail_delete
import foliary.foliary.generated.resources.task_detail_delete_dialog_confirm
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_1
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_2
import foliary.foliary.generated.resources.task_detail_delete_dialog_title
import foliary.foliary.generated.resources.task_detail_due_date_label
import foliary.foliary.generated.resources.task_detail_due_date_period
import foliary.foliary.generated.resources.task_detail_edit
import foliary.foliary.generated.resources.task_detail_mark_completed
import foliary.foliary.generated.resources.task_detail_no_description
import foliary.foliary.generated.resources.task_detail_status_completed
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskDetailScreen(
    viewData: TaskDetailViewData,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    AnimatedContent(targetState = viewData, contentKey = { it::class }) {
        when (viewData) {
            TaskDetailViewData.Idle -> {}
            TaskDetailViewData.Loading -> LoadingIndicator()
            is TaskDetailViewData.Loaded -> TaskDetailContent(
                task = viewData.task,
                onEvent = onEvent,
            )
        }
    }
}

@Composable
private fun TaskDetailTopBar(
    isComplete: Boolean,
    taskTitle: String,
    onEvent: (TaskDetailEvent) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        CompleteButton(isComplete, onEvent)
        MenuButton(taskTitle, onEvent)
    }
}

@Composable
private fun CompleteButton(isComplete: Boolean, onEvent: (TaskDetailEvent) -> Unit) {
    AnimatedContent(targetState = isComplete) { complete ->
        if (complete) {
            TextButton(onClick = { onEvent(TaskDetailEvent.MarkNotCompletedClicked) }) {
                Icon(
                    imageVector = Lucide.CircleCheckBig,
                    contentDescription = stringResource(Res.string.task_detail_mark_completed)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(Res.string.task_detail_status_completed))
            }
        } else {
            FilledTonalButton(onClick = { onEvent(TaskDetailEvent.MarkCompletedClicked) }) {
                Icon(
                    imageVector = Lucide.CircleCheck,
                    contentDescription = stringResource(Res.string.task_detail_mark_completed)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(Res.string.task_detail_mark_completed))
            }
        }
    }
}

@Composable
private fun MenuButton(taskTitle: String, onEvent: (TaskDetailEvent) -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        FoliaryMenuIconButton(onClick = { showMenu = showMenu.not() })

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(Res.string.task_detail_edit)) },
                leadingIcon = { Icon(Lucide.Pencil, null) },
                onClick = {
                    showMenu = false
                    onEvent(TaskDetailEvent.EditClicked)
                }
            )

            MenuButtonDelete(taskTitle = taskTitle) {
                showMenu = false
                onEvent(TaskDetailEvent.DeleteClicked)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun MenuButtonDelete(taskTitle: String, onDeleteClick: () -> Unit) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    DropdownMenuItem(
        text = { Text(text = stringResource(Res.string.task_detail_delete)) },
        leadingIcon = { Icon(Lucide.Trash, null) },
        onClick = { showDeleteDialog = true },
        colors = MenuDefaults.itemColors(
            textColor = MaterialTheme.colorScheme.error,
            leadingIconColor = MaterialTheme.colorScheme.error
        )
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Lucide.Trash,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            title = { Text(text = stringResource(Res.string.task_detail_delete_dialog_title)) },
            text = {
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(Res.string.task_detail_delete_dialog_message_1))
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(" \"$taskTitle\" ")
                        }
                        append(stringResource(Res.string.task_detail_delete_dialog_message_2))
                    }
                )
            },
            confirmButton = {
                FilledTonalButton(
                    onClick = onDeleteClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(text = stringResource(Res.string.task_detail_delete_dialog_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(text = stringResource(Res.string.general_cancel))
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
private fun TaskDetailContent(
    task: TaskDetailViewData.Loaded.TaskViewData,
    onEvent: (TaskDetailEvent) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        TaskDetailTopBar(isComplete = task.isComplete, taskTitle = task.title, onEvent = onEvent)
        Spacer(Modifier.height(8.dp))
        Text(text = task.title, style = MaterialTheme.typography.titleLarge)
        PillsRow(task)
        TaskDetailDescription(task.description)
        HorizontalDivider()
        TaskDetailList(task)
    }
}

@Composable
fun PillsRow(task: TaskDetailViewData.Loaded.TaskViewData) {
    FlowRow(
        modifier = Modifier.padding(top = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AnimatedVisibility(visible = task.isOverdue) { OverduePill() }
        PriorityPill(task.priority)
    }
}

@Composable
private fun TaskDetailDescription(description: String?) {
    val text = description ?: stringResource(Res.string.task_detail_no_description)
    Text(
        modifier = Modifier.padding(vertical = 16.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun TaskDetailList(task: TaskDetailViewData.Loaded.TaskViewData) {
    Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        CreationDateDetailRow(task.creationDate)
        task.dueDate?.let { dueDate ->
            DueDateDetailRow(
                dueDate,
                task.isOverdue,
                task.overduePeriodInDays
            )
        }
        task.completionDate?.let { completionDate -> CompletionDateDetailRow(completionDate) }
    }
}

@Composable
private fun CreationDateDetailRow(creationDate: String) {
    DetailRow(
        icon = Lucide.Calendar,
        label = stringResource(Res.string.task_detail_creation_date_label)
    ) {
        Text(
            text = creationDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CompletionDateDetailRow(completionDate: String) {
    DetailRow(
        icon = Lucide.CalendarCheck,
        label = stringResource(Res.string.task_detail_completion_date_label)
    ) {
        Text(
            text = completionDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun DueDateDetailRow(dueDate: String, overdue: Boolean, overduePeriodInDays: Long?) {
    DetailRow(
        icon = Lucide.CalendarClock,
        label = stringResource(Res.string.task_detail_due_date_label)
    ) {
        Text(
            text = buildString {
                append(dueDate)
                overduePeriodInDays?.let { period ->
                    append(" · ")
                    append(stringResource(Res.string.task_detail_due_date_period, period))
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = if (overdue) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = null
            )

            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        content()
    }
}
