package dev.appoutlet.foliary.core.ui.priority

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.lucide.ChevronDown
import com.composables.icons.lucide.ChevronUp
import com.composables.icons.lucide.ChevronsDown
import com.composables.icons.lucide.ChevronsUp
import com.composables.icons.lucide.ChevronsUpDown
import com.composables.icons.lucide.CircleMinus
import com.composables.icons.lucide.Lucide
import dev.appoutlet.foliary.data.task.database.entity.Priority
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.priority_pill_blocker
import foliary.foliary.generated.resources.priority_pill_high
import foliary.foliary.generated.resources.priority_pill_highest
import foliary.foliary.generated.resources.priority_pill_low
import foliary.foliary.generated.resources.priority_pill_lowest
import foliary.foliary.generated.resources.priority_pill_medium
import org.jetbrains.compose.resources.stringResource

data class PriorityViewData(
    val text: String,
    val color: Color,
    val icon: ImageVector,
)

val PriorityHighLight = Color(0xFFBD8100)
val PriorityHighestLight = Color(0xFFA65200)
val PriorityBlockerLight = Color(0xFF93000A)

val PriorityHighDark = Color(0xFFFFAA5C)
val PriorityHighestDark = Color(0xFFFF8080)
val PriorityBlockerDark = Color(0xFFFF5449)

private data class PriorityColors(
    val high: Color,
    val highest: Color,
    val blocker: Color
)

@Composable
fun Priority.viewData(): PriorityViewData {
    val backgroundColor = MaterialTheme.colorScheme.background

    val colors = remember(this) {
        val isDarkTheme = backgroundColor.luminance() < .5f
        if (isDarkTheme) {
            PriorityColors(PriorityHighDark, PriorityHighestDark, PriorityBlockerDark)
        } else {
            PriorityColors(PriorityHighLight, PriorityHighestLight, PriorityBlockerLight)
        }
    }

    return when (this) {
        Priority.LOWEST -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_lowest),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .4f),
            icon = Lucide.ChevronsDown,
        )

        Priority.LOW -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_low),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f),
            icon = Lucide.ChevronDown,
        )

        Priority.MEDIUM -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_medium),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f),
            icon = Lucide.ChevronsUpDown,
        )

        Priority.HIGH -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_high),
            color = colors.high,
            icon = Lucide.ChevronUp,
        )

        Priority.HIGHEST -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_highest),
            color = colors.highest,
            icon = Lucide.ChevronsUp,
        )

        Priority.BLOCKER -> PriorityViewData(
            text = stringResource(Res.string.priority_pill_blocker),
            color = colors.blocker,
            icon = Lucide.CircleMinus,
        )
    }
}
