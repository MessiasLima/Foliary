package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

private const val FiftyPercent = 50

@Composable
fun FoliaryPrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .dropShadow(
                shape = MaterialTheme.shapes.extraLarge,
                shadow = Shadow(
                    radius = 60.dp,
                    spread = 0.dp,
                    offset = DpOffset(0.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = .1f),
                )
            ),
        enabled = enabled,
        shape = RoundedCornerShape(FiftyPercent),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        content = content
    )
}

@Composable
fun FoliarySecondaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .dropShadow(
                shape = MaterialTheme.shapes.extraLarge,
                shadow = Shadow(
                    radius = 60.dp,
                    spread = 0.dp,
                    offset = DpOffset(0.dp, 0.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = .1f),
                )
            ),
        enabled = enabled,
        shape = RoundedCornerShape(FiftyPercent),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary
        ),
        content = content
    )
}

@Composable
fun FoliaryOutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(percent = FiftyPercent),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        content = content
    )
}
