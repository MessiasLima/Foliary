package dev.appoutlet.foliary.feature.createtask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.Flag
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.core.ui.component.card.FoliaryCard
import dev.appoutlet.foliary.core.ui.component.pill.PriorityPill
import dev.appoutlet.foliary.data.task.database.entity.Priority
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.priority_pill_blocker
import foliary.foliary.generated.resources.priority_pill_high
import foliary.foliary.generated.resources.priority_pill_highest
import foliary.foliary.generated.resources.priority_pill_low
import foliary.foliary.generated.resources.priority_pill_lowest
import foliary.foliary.generated.resources.priority_pill_medium
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoliaryPriorityPicker(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    Box(modifier = modifier) {
        PriorityPickerCard(
            label = label,
            selectedPriority = selectedPriority,
            onClick = { showMenu = true }
        )

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(text = stringResource(priority.stringResource)) },
                    leadingIcon = { Icon(imageVector = Lucide.Flag, contentDescription = null) },
                    trailingIcon = if (priority == selectedPriority) {
                        { Icon(imageVector = Lucide.Check, contentDescription = null) }
                    } else {
                        null
                    },
                    onClick = {
                        showMenu = false
                        onPrioritySelected(priority)
                    }
                )
            }
        }
    }
}

@Composable
private fun PriorityPickerCard(
    label: String,
    selectedPriority: Priority,
    onClick: () -> Unit,
) {
    FoliaryCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("FoliaryPriorityPicker:Card")
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                PriorityPill(
                    priority = selectedPriority,
                    modifier = Modifier.testTag("FoliaryPriorityPicker:Value")
                )
            }

            Icon(
                modifier = Modifier.padding(end = 16.dp),
                imageVector = Lucide.ChevronDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private val Priority.stringResource: StringResource
    get() = when (this) {
        Priority.LOWEST -> Res.string.priority_pill_lowest
        Priority.LOW -> Res.string.priority_pill_low
        Priority.MEDIUM -> Res.string.priority_pill_medium
        Priority.HIGH -> Res.string.priority_pill_high
        Priority.HIGHEST -> Res.string.priority_pill_highest
        Priority.BLOCKER -> Res.string.priority_pill_blocker
    }
