package dev.appoutlet.foliary.core.ui.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.modifier.FoliaryShadowColorDefault
import dev.appoutlet.foliary.core.ui.component.modifier.foliaryShadow

@Composable
fun FoliaryCard(
    modifier: Modifier = Modifier,
    colors: FoliaryCardColors = FoliaryCardDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.foliaryShadow(color = colors.shadowColor),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = colors.containerColor,
            contentColor = colors.contentColor,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        content = content
    )
}

object FoliaryCardDefaults {

    @Composable
    fun colors(
        containerColor: Color = MaterialTheme.colorScheme.surface,
        contentColor: Color = MaterialTheme.colorScheme.onSurface,
        shadowColor: Color = FoliaryShadowColorDefault
    ) = FoliaryCardColors(
        containerColor = containerColor,
        contentColor = contentColor,
        shadowColor = shadowColor,
    )
}

data class FoliaryCardColors(
    val containerColor: Color,
    val contentColor: Color,
    val shadowColor: Color,
)
