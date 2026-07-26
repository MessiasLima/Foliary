package dev.appoutlet.foliary.feature.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
        val profileLazyListState = rememberLazyListState()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    NavigationBarItem(
                        selected = viewData.selectedTab == MainTab.Today,
                        onClick = { viewModel.onTabSelected(MainTab.Today) },
                        icon = { Icon(Lucide.CalendarCheck, contentDescription = null) },
                        label = { Text(stringResource(Res.string.main_nav_today)) },
                        colors = navigationBarItemColors(),
                    )

                    NavigationBarItem(
                        selected = viewData.selectedTab == MainTab.Upcoming,
                        onClick = { viewModel.onTabSelected(MainTab.Upcoming) },
                        icon = { Icon(Lucide.CalendarDays, contentDescription = null) },
                        label = { Text(stringResource(Res.string.main_nav_upcoming)) },
                        colors = navigationBarItemColors(),
                    )

                    NavigationBarItem(
                        selected = viewData.selectedTab == MainTab.Profile,
                        onClick = { viewModel.onTabSelected(MainTab.Profile) },
                        icon = { Icon(Lucide.User, contentDescription = null) },
                        label = { Text(stringResource(Res.string.main_nav_profile)) },
                        colors = navigationBarItemColors(),
                    )
                }
            },
        ) { _ ->
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = viewData.selectedTab,
            ) { selectedTab ->
                when (selectedTab) {
                    MainTab.Today -> TodayScreen(todayLazyListState)
                    MainTab.Upcoming -> UpcomingScreen(upcomingLazyListState)
                    MainTab.Profile -> ProfileScreen(profileLazyListState)
                }
            }
        }
    }
}

@Composable
private fun navigationBarItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = MaterialTheme.colorScheme.primary,
    selectedTextColor = MaterialTheme.colorScheme.primary,
    indicatorColor = MaterialTheme.colorScheme.secondary,
    unselectedIconColor = MaterialTheme.colorScheme.onBackground,
    unselectedTextColor = MaterialTheme.colorScheme.onBackground,
)

expect fun getWindowDecorationPadding(): Dp
