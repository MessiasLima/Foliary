package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.back_icon_button_a11y
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoliaryMenuIconButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = menuIcon,
            contentDescription = stringResource(Res.string.back_icon_button_a11y),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

expect val menuIcon: ImageVector
