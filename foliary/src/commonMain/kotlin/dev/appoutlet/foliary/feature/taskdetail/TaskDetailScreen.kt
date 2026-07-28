package dev.appoutlet.foliary.feature.taskdetail

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Square
import com.composables.icons.lucide.SquareCheckBig
import com.composables.icons.lucide.Trash
import dev.appoutlet.foliary.core.ui.component.button.FoliaryMenuIconButton
import dev.appoutlet.foliary.core.ui.component.layout.LoadingIndicator
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.general_cancel
import foliary.foliary.generated.resources.task_detail_delete
import foliary.foliary.generated.resources.task_detail_delete_dialog_confirm
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_1
import foliary.foliary.generated.resources.task_detail_delete_dialog_message_2
import foliary.foliary.generated.resources.task_detail_delete_dialog_title
import foliary.foliary.generated.resources.task_detail_edit
import foliary.foliary.generated.resources.task_detail_mark_completed
import foliary.foliary.generated.resources.task_detail_no_description
import foliary.foliary.generated.resources.task_detail_status_completed
import org.jetbrains.compose.resources.stringResource

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
                    imageVector = Lucide.SquareCheckBig,
                    contentDescription = stringResource(Res.string.task_detail_mark_completed)
                )
                Spacer(Modifier.width(8.dp))
                Text(text = stringResource(Res.string.task_detail_status_completed))
            }
        } else {
            FilledTonalButton(
                onClick = { onEvent(TaskDetailEvent.MarkCompletedClicked) },
            ) {
                Icon(
                    imageVector = Lucide.Square,
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

        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(
                text = { Text(text = stringResource(Res.string.task_detail_edit)) },
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
        onClick = { showDeleteDialog = true }
    )

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape
                        )
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
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = task.title,
            style = MaterialTheme.typography.titleLarge
        )
        TaskDetailDescription(task.description)
        HorizontalDivider(Modifier.padding(vertical = 16.dp))
    }
}

@Composable
private fun TaskDetailDescription(description: String?) {
    val text = description ?: stringResource(Res.string.task_detail_no_description)
    Text(
        modifier = Modifier.padding(top = 16.dp),
        text = text,
        style = MaterialTheme.typography.bodyMedium
    )
}
