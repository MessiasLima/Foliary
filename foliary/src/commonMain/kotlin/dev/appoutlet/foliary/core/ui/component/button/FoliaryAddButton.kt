package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Plus
import dev.appoutlet.foliary.core.ui.component.modifier.FoliaryShadowColorDefault
import dev.appoutlet.foliary.core.ui.component.modifier.foliaryShadow

@Composable
fun FoliaryAddButton(
    onClick: () -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    showShadow: Boolean = false,
) {
    val iconButtonShadowColor by animateColorAsState(
        targetValue = if (showShadow) FoliaryShadowColorDefault else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val iconButtonBorderColor by animateColorAsState(
        targetValue = if (showShadow) MaterialTheme.colorScheme.outline else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        OutlinedIconButton(
            modifier = Modifier.foliaryShadow(color = iconButtonShadowColor),
            onClick = onClick,
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(width = 1.dp, color = iconButtonBorderColor)
        ) {
            Icon(
                imageVector = Lucide.Plus,
                contentDescription = contentDescription
            )
        }
    }
}
