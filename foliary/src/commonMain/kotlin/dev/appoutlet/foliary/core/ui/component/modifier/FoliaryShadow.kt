package dev.appoutlet.foliary.core.ui.component.modifier

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

val FoliaryShadowColorDefault: Color
    @Composable get() = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)

@Composable
fun Modifier.foliaryShadow(
    shape: Shape = MaterialTheme.shapes.extraLarge,
    radius: Dp = 60.dp,
    spread: Dp = 10.dp,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    color: Color = FoliaryShadowColorDefault,
): Modifier {
    val mods = Modifier.dropShadow(
        shape = shape,
        shadow = Shadow(radius = radius, spread = spread, offset = offset, color = color)
    )
    return this.then(mods)
}
