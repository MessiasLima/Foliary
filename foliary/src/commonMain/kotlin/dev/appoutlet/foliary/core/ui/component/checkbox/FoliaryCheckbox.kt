package dev.appoutlet.foliary.core.ui.component.checkbox

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Check
import com.composables.icons.lucide.Lucide

@Composable
fun FoliaryCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val containerColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.background
        },
        label = "FoliaryCheckboxContainerColor",
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
        label = "FoliaryCheckboxBorderColor",
    )

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .toggleable(
                value = checked,
                enabled = enabled && onCheckedChange != null,
                role = Role.Checkbox,
                onValueChange = { onCheckedChange?.invoke(it) },
            )
            .background(containerColor)
            .border(BorderStroke(2.dp, borderColor), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (checked) {
            Checkmark(color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
private fun Checkmark(color: Color) {
    Icon(
        modifier = Modifier.size(14.dp),
        imageVector = Lucide.Check,
        contentDescription = null,
        tint = color
    )
}
