package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCardDefaults
import dev.appoutlet.foliary.core.ui.component.checkbox.FoliaryCheckbox
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_item_overdue
import org.jetbrains.compose.resources.stringResource

@Composable
fun TaskItem(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    isCompleted: Boolean = false,
    onCompletedChange: (Boolean) -> Unit = { },
    onStartClick: () -> Unit = {},
    isOverdue: Boolean = false,
) {
    val titleColor = animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = CompletedAlpha)
        } else {
            MaterialTheme.colorScheme.primary
        },
    )

    val cardContainerColor = animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.surfaceDim
        } else {
            MaterialTheme.colorScheme.surface
        },
    )

    FoliaryCard(
        modifier = modifier.fillMaxWidth(),
        colors = FoliaryCardDefaults.colors(containerColor = cardContainerColor.value),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FoliaryCheckbox(
                checked = isCompleted,
                onCheckedChange = onCompletedChange,
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
                    color = titleColor.value,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else null,
                    maxLines = MaxTitleLines,
                    overflow = TextOverflow.Ellipsis,
                )

                description?.let {
                    Text(
                        text = it,
                        modifier = Modifier.testTag("TaskItem:Description"),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = MaxDescriptionLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Row {
                    if (isOverdue && !isCompleted) {
                        OverduePill()
                    }
                }
            }

            StartButton(onClick = onStartClick)
        }
    }
}

@Composable
private fun OverduePill() {
    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.surfaceDim, shape = CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .testTag("TaskItem:OverduePill"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            modifier = Modifier.size(12.dp),
            imageVector = Lucide.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = stringResource(Res.string.task_item_overdue),
            modifier = Modifier,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Icon(
            imageVector = Lucide.Play,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

private const val CompletedAlpha = 0.38f
private const val MaxDescriptionLines = 3
private const val MaxTitleLines = 2
