package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Lucide

@Composable
fun UrlPill(
    url: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    FoliaryPill(
        modifier = modifier,
        text = url,
        icon = Lucide.Link,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
