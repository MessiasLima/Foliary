package dev.appoutlet.foliary.core.ui.component.task

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCardDefaults
import dev.appoutlet.foliary.core.ui.component.checkbox.FoliaryCheckbox
import dev.appoutlet.foliary.core.ui.component.pill.OverduePill

@Composable
fun FoliaryTaskCard(
    task: FoliaryTaskCardViewData,
    modifier: Modifier = Modifier,
    onCompletedChange: (Boolean) -> Unit = { },
    onClick: (() -> Unit)? = null,
) {
    val cardContainerColor = animateColorAsState(
        targetValue = if (task.isCompleted) {
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
                .clickable(enabled = onClick != null) { onClick?.invoke() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            FoliaryCheckbox(
                checked = task.isCompleted,
                onCheckedChange = onCompletedChange,
                modifier = Modifier.testTag("FoliaryTaskCard:Checkbox"),
            )

            FoliaryTaskCardContent(
                task = task,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun FoliaryTaskCardContent(
    task: FoliaryTaskCardViewData,
    modifier: Modifier = Modifier,
) {
    val titleColor = animateColorAsState(
        targetValue = if (task.isCompleted) {
            MaterialTheme.colorScheme.onSurface.copy(alpha = CompletedAlpha)
        } else {
            MaterialTheme.colorScheme.primary
        },
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = task.title,
            modifier = Modifier.testTag("FoliaryTaskCard:Title"),
            style = MaterialTheme.typography.titleMedium,
            color = titleColor.value,
            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
            maxLines = MaxTitleLines,
            overflow = TextOverflow.Ellipsis,
        )

        task.description?.let {
            Text(
                text = it,
                modifier = Modifier.testTag("FoliaryTaskCard:Description"),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = MaxDescriptionLines,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Row {
            if (task.isOverdue && !task.isCompleted) { OverduePill() }
        }
    }
}

private const val CompletedAlpha = 0.38f
private const val MaxDescriptionLines = 3
private const val MaxTitleLines = 2
