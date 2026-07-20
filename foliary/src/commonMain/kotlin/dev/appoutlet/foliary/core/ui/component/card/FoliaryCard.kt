package dev.appoutlet.foliary.core.ui.component.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun FoliaryCard(
    modifier: Modifier = Modifier,
    colors: CardColors = FoliaryCardDefaults.colors(),
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier
            .dropShadow(
                shape = MaterialTheme.shapes.extraLarge,
                shadow = Shadow(
                    radius = 60.dp,
                    spread = 10.dp,
                    offset = DpOffset(0.dp, 0.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                )
            ),
        shape = MaterialTheme.shapes.extraLarge,
        colors = colors,
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
    ): CardColors = CardDefaults.cardColors(
        containerColor = containerColor,
        contentColor = contentColor,
    )
}
