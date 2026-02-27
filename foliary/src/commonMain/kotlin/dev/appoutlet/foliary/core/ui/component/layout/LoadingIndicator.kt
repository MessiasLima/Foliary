package dev.appoutlet.foliary.core.ui.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.appoutlet.foliary.core.ui.component.modifier.widthInNarrow

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier, message: String? = null) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.fillMaxWidth().widthInNarrow().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator()

            message?.let {
                Spacer(Modifier.size(16.dp))
                Text(text = it, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}