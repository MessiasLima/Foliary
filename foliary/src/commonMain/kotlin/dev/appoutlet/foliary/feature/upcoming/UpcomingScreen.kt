package dev.appoutlet.foliary.feature.upcoming

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.upcoming_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpcomingScreen(lazyListState: LazyListState) {
    LazyColumn(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .fillMaxSize(),
        state = lazyListState,
    ) {
        item {
            Text(
                modifier = Modifier.padding(16.dp),
                text = stringResource(Res.string.upcoming_title),
                style = MaterialTheme.typography.displaySmall
            )
        }
    }
}
