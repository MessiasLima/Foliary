package dev.appoutlet.foliary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.appoutlet.foliary.core.navigation.LocalNavigator
import dev.appoutlet.foliary.core.navigation.Navigator
import dev.appoutlet.foliary.core.navigation.getSavedStateConfiguration
import dev.appoutlet.foliary.feature.common.NavigationAggregator
import dev.appoutlet.foliary.feature.signin.SignInNavKey
import org.koin.compose.koinInject

@Composable
fun Navigation() {
    val navigationAggregator = koinInject<NavigationAggregator>()
    val config = remember(navigationAggregator) { getSavedStateConfiguration(navigationAggregator.navigation) }
    val backStack = rememberNavBackStack(configuration = config, SignInNavKey)
    CompositionLocalProvider(LocalNavigator provides Navigator(backStack)) {
        NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
            ),
            entryProvider = entryProvider {
                for (navigation in navigationAggregator.navigation) { navigation.setupRoute(this) }
            },
        )
    }
}
