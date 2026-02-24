package dev.appoutlet.foliary

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.appoutlet.foliary.theme.FoliaryTheme

@Preview
@Composable
fun App() = FoliaryTheme {
    Text(text = "Hello, Foliary!", style = MaterialTheme.typography.displayMedium)
}
