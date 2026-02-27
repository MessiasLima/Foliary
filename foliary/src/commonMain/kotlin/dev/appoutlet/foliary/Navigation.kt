package dev.appoutlet.foliary

import androidx.compose.runtime.Composable
import dev.appoutlet.foliary.feature.common.NavigationAggregator
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val navigationAggregator = koinInject<NavigationAggregator>()
    navigationAggregator.navigation.forEach {
        println(it.toString())
    }
}

