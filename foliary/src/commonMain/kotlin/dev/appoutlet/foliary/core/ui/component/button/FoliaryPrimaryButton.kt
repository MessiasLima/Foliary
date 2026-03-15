package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme

private const val FiftyPercent = 50

/**
 * A custom primary button for Foliary, styled according to the app's branding.
 *
 * @param onClick Callback to be invoked when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When false, the button will not be clickable.
 * @param content The content of the button (typically a RowScope composable lambda).
 */
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

/**
 * Button preview for FoliaryPrimaryButton.
 */
@Preview
@Composable
private fun FoliaryPrimaryButtonPreview() {
    FoliaryTheme {
        FoliaryPrimaryButton(onClick = {}) {
            Text("Primary")
        }
    }
}
