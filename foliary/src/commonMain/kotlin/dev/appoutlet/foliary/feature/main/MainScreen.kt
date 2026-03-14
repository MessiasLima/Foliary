package dev.appoutlet.foliary.feature.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    Scaffold {
        Button(modifier = Modifier.padding(it), onClick = {
            viewModel.logOut()
        }) {
            Text("Log out")
        }
    }
}