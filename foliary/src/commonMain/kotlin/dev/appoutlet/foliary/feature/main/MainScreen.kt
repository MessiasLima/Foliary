package dev.appoutlet.foliary.feature.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfoV2
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CalendarCheck
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.feature.profile.ProfileScreen
import dev.appoutlet.foliary.feature.today.TodayScreen
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.main_nav_profile
import foliary.foliary.generated.resources.main_nav_today
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    Screen(
        screenName = "MainScreen",
        viewModelProvider = { viewModel }
    ) { viewData: MainViewData ->
        val itemColors = getItemColors()

        val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
            adaptiveInfo = currentWindowAdaptiveInfoV2(),
        )

        val itemTopPadding = remember(layoutType) {
            when (layoutType) {
                NavigationSuiteType.NavigationRail,
                NavigationSuiteType.WideNavigationRailExpanded,
                NavigationSuiteType.WideNavigationRailCollapsed -> 16.dp
                else -> 0.dp
            }
        }

        NavigationSuiteScaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
            layoutType = layoutType,
            navigationSuiteItems = {
                item(
                    modifier = Modifier.padding(top = itemTopPadding),
                    selected = viewData.selectedTab == MainTab.Today,
                    onClick = { viewModel.onTabSelected(MainTab.Today) },
                    icon = { Icon(Lucide.CalendarCheck, contentDescription = null) },
                    label = { Text(stringResource(Res.string.main_nav_today)) },
                    colors = itemColors,
                )

                item(
                    modifier = Modifier.padding(top = itemTopPadding),
                    selected = viewData.selectedTab == MainTab.Profile,
                    onClick = { viewModel.onTabSelected(MainTab.Profile) },
                    icon = { Icon(Lucide.User, contentDescription = null) },
                    label = { Text(stringResource(Res.string.main_nav_profile)) },
                    colors = itemColors,
                )
            },
            navigationSuiteColors = NavigationSuiteDefaults.colors(
                navigationBarContainerColor = MaterialTheme.colorScheme.surface,
                navigationRailContainerColor = MaterialTheme.colorScheme.surface,
            ),
        ) {
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = viewData.selectedTab,
            ) { selectedTab ->
                when (selectedTab) {
                    MainTab.Today -> TodayScreen()
                    MainTab.Profile -> ProfileScreen()
                }
            }
        }
    }
}

@Composable
private fun getItemColors(): NavigationSuiteItemColors {
    return NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.secondary,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.secondary,
            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(),
    )
}
