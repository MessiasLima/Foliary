package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.button.FoliarySecondaryButton
import dev.appoutlet.foliary.core.ui.component.modifier.widthInCompact

@Composable
fun FoliaryEmptyContent(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    image: Painter? = null,
    actionLabel: String? = null,
    onActionClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .widthInCompact()
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 56.dp),
        verticalArrangement = Arrangement.Center
    ) {
        image?.let {
            Image(
                modifier = Modifier.height(192.dp).align(Alignment.CenterHorizontally),
                painter = it,
                contentDescription = null
            )
        }

        Text(
            modifier = Modifier.padding(top = if (image != null) 24.dp else 0.dp, bottom = 8.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium
        )

        actionLabel?.let {
            FoliarySecondaryButton(
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 16.dp),
                onClick = onActionClick,
            ) {
                Text(text = it)
            }
        }
    }
}
