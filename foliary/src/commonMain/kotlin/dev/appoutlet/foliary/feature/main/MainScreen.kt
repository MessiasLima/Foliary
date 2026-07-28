package dev.appoutlet.foliary.feature.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.CalendarCheck
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.User
import dev.appoutlet.foliary.core.ui.component.layout.Screen
import dev.appoutlet.foliary.feature.profile.ProfileScreen
import dev.appoutlet.foliary.feature.today.TodayScreen
import dev.appoutlet.foliary.feature.upcoming.UpcomingScreen
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.main_nav_profile
import foliary.foliary.generated.resources.main_nav_today
import foliary.foliary.generated.resources.main_nav_upcoming
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    Screen(
        screenName = "MainScreen",
        viewModelProvider = { viewModel }
    ) { viewData: MainViewData ->
        val todayLazyListState = rememberLazyListState()
        val upcomingLazyListState = rememberLazyListState()

        MainScreenNavigation(
            selectedTab = viewData.selectedTab,
            onTabSelect = viewModel::onTabSelected
        ) { selectedTab ->
            when (selectedTab) {
                MainTab.Today -> TodayScreen(todayLazyListState)
                MainTab.Upcoming -> UpcomingScreen(upcomingLazyListState)
                MainTab.Profile -> ProfileScreen()
            }
        }
    }
}

@Composable
private fun MainScreenNavigation(
    selectedTab: MainTab,
    onTabSelect: (MainTab) -> Unit,
    content: @Composable (MainTab) -> Unit
) {
    val itemColors = getItemColors()

    val layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(
        adaptiveInfo = currentWindowAdaptiveInfoV2(),
    )

    val (windowDecorationPadding, itemTopPadding) = remember(layoutType) {
        when (layoutType) {
            NavigationSuiteType.NavigationRail,
            NavigationSuiteType.WideNavigationRailExpanded,
            NavigationSuiteType.WideNavigationRailCollapsed -> getWindowDecorationPadding() to 16.dp

            else -> 0.dp to 0.dp
        }
    }

    NavigationSuiteScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface),
        layoutType = layoutType,
        navigationSuiteItems = {
            item(
                modifier = Modifier.testTag("MainScreen:TodayTab")
                    .padding(top = windowDecorationPadding + itemTopPadding),
                selected = selectedTab == MainTab.Today,
                onClick = { onTabSelect(MainTab.Today) },
                icon = { Icon(Lucide.CalendarCheck, contentDescription = null) },
                label = { Text(stringResource(Res.string.main_nav_today)) },
                colors = itemColors,
            )

            item(
                modifier = Modifier.testTag("MainScreen:UpcomingTab").padding(top = itemTopPadding),
                selected = selectedTab == MainTab.Upcoming,
                onClick = { onTabSelect(MainTab.Upcoming) },
                icon = { Icon(Lucide.CalendarDays, contentDescription = null) },
                label = { Text(stringResource(Res.string.main_nav_upcoming)) },
                colors = itemColors,
            )

            item(
                modifier = Modifier.testTag("MainScreen:ProfileTab").padding(top = itemTopPadding),
                selected = selectedTab == MainTab.Profile,
                onClick = { onTabSelect(MainTab.Profile) },
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
            targetState = selectedTab,
            content = content
        )
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

expect fun getWindowDecorationPadding(): Dp
