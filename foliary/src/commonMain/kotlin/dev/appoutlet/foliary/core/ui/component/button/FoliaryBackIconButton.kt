package dev.appoutlet.foliary.core.ui.component.button

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.back_icon_button_a11y
import org.jetbrains.compose.resources.stringResource

@Composable
fun FoliaryBackIconButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = backIcon,
            contentDescription = stringResource(Res.string.back_icon_button_a11y)
        )
    }
}

expect val backIcon: ImageVector
