package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_item_overdue
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TaskItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    isCompleted: Boolean = false,
    isOverdue: Boolean = false,
) {
    FoliaryCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Checkbox(
                checked = isCompleted,
                onCheckedChange = null,
                modifier = Modifier.testTag("TaskItem:Checkbox"),
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    modifier = Modifier.testTag("TaskItem:Title"),
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isCompleted) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = CompletedAlpha)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                description?.let {
                    Text(
                        text = it,
                        modifier = Modifier.testTag("TaskItem:Description"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = MaxDescriptionLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                if (isOverdue && !isCompleted) {
                    OverduePill()
                }
            }
        }
    }
}

@Composable
private fun OverduePill() {
    Surface(
        modifier = Modifier
            .padding(top = 4.dp)
            .testTag("TaskItem:OverduePill"),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
    ) {
        Text(
            text = stringResource(Res.string.task_item_overdue),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

private const val CompletedAlpha = 0.38f
private const val MaxDescriptionLines = 3

@Preview
@Composable
private fun TaskItemPreview() {
    FoliaryTheme {
        TaskItem(
            title = "Write API Documentation",
            description = "Draft the OpenAPI spec for the new endpoints",
        )
    }
}

@Preview
@Composable
private fun TaskItemOverduePreview() {
    FoliaryTheme {
        TaskItem(
            title = "Pay electricity bill",
            isOverdue = true,
        )
    }
}

@Preview
@Composable
private fun TaskItemCompletedPreview() {
    FoliaryTheme {
        TaskItem(
            title = "Inbox Zero",
            description = "Process all unread emails and archive newsletters",
            isCompleted = true,
        )
    }
}
