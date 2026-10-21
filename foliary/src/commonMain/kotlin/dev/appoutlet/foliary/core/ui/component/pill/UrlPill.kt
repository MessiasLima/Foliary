package dev.appoutlet.foliary.core.ui.component.pill

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Link
import com.composables.icons.lucide.Lucide

@Composable
fun UrlPill(url: String, modifier: Modifier = Modifier) {
    FoliaryPill(
        modifier = modifier,
        text = url,
        icon = Lucide.Link,
    )
}
