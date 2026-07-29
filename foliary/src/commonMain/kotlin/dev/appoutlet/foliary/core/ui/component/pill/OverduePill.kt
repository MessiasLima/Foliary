package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Info
import com.composables.icons.lucide.Lucide
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.task_item_overdue
import org.jetbrains.compose.resources.stringResource

@Composable
fun OverduePill(modifier: Modifier = Modifier) {
    FoliaryPill(
        modifier = modifier,
        text = stringResource(Res.string.task_item_overdue),
        contentColor = MaterialTheme.colorScheme.error,
        icon = Lucide.Info
    )
}
