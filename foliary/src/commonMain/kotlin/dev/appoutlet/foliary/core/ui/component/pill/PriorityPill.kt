package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.priority.viewData
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import dev.appoutlet.foliary.data.task.database.entity.Priority
import org.jetbrains.compose.resources.stringResource

@Composable
fun PriorityPill(priority: Priority, modifier: Modifier = Modifier) {
    val priorityViewData = priority.viewData()

    FoliaryPill(
        modifier = modifier.testTag("PriorityPill"),
        icon = priorityViewData.icon,
        text = priorityViewData.text,
        contentColor = priorityViewData.color
    )
}

@Composable
@Preview
private fun PriorityPillPreview() {
    FoliaryTheme {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Priority.entries.forEach {
                PriorityPill(it)
            }
        }
    }
}
