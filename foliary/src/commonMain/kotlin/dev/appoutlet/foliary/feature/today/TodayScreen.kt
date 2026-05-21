package dev.appoutlet.foliary.feature.today

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.today_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodayScreen() {
    val viewModel = koinViewModel<TodayViewModel>()
    Screen(
        screenName = "TodayScreen",
        viewModelProvider = { viewModel }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier.padding(paddingValues).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(Res.string.today_title))
            }
        }
    }
}
