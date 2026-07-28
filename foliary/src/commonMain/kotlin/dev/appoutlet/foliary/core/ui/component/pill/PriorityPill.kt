package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Flag
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import dev.appoutlet.foliary.data.task.database.entity.Priority
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.priority_pill_blocker
import foliary.foliary.generated.resources.priority_pill_high
import foliary.foliary.generated.resources.priority_pill_highest
import foliary.foliary.generated.resources.priority_pill_low
import foliary.foliary.generated.resources.priority_pill_lowest
import foliary.foliary.generated.resources.priority_pill_medium
import org.jetbrains.compose.resources.stringResource

private val HighContentColor = Color(0xFFC4A319)
private val HighestContentColor =  Color(0xFFAD5203)

@Composable
fun PriorityPill(priority: Priority) {
    val defaultContentColor = MaterialTheme.colorScheme.onSurface
    val blockerContentColor = MaterialTheme.colorScheme.error

    val (text, contentColor) = when (priority) {
        Priority.LOWEST -> Res.string.priority_pill_lowest to defaultContentColor
        Priority.LOW -> Res.string.priority_pill_low to defaultContentColor
        Priority.MEDIUM -> Res.string.priority_pill_medium to defaultContentColor
        Priority.HIGH -> Res.string.priority_pill_high to HighContentColor
        Priority.HIGHEST -> Res.string.priority_pill_highest to HighestContentColor
        Priority.BLOCKER -> Res.string.priority_pill_blocker to blockerContentColor
    }

    FoliaryPill(
        icon = Lucide.Flag,
        text = stringResource(text),
        contentColor = contentColor
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
