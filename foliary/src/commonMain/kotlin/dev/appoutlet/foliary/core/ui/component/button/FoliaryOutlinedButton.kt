package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme

private const val FiftyPercent = 50

/**
 * A custom outlined button for Foliary, styled according to the app's design system.
 *
 * @param onClick Callback to be invoked when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Controls the enabled state of the button. When false, the button will not be clickable.
 * @param content The content of the button (typically a RowScope composable lambda).
 */
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

/**
 * Button preview for FoliaryOutlinedButton.
 */
@Preview
@Composable
private fun FoliaryOutlinedButtonPreview() {
    FoliaryTheme {
        FoliaryOutlinedButton(onClick = {}) {
            Text("Outlined")
        }
    }
}
