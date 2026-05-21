package dev.appoutlet.foliary.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Settings
import com.composables.icons.lucide.Sun
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
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                item(
                    selected = viewData.selectedTab == MainTab.Today,
                    onClick = { viewModel.onTabSelected(MainTab.Today) },
                    icon = { Icon(Lucide.Sun, contentDescription = null) },
                    label = { Text(stringResource(Res.string.main_nav_today)) }
                )

                item(
                    selected = viewData.selectedTab == MainTab.Profile,
                    onClick = { viewModel.onTabSelected(MainTab.Profile) },
                    icon = { Icon(Lucide.Settings, contentDescription = null) },
                    label = { Text(stringResource(Res.string.main_nav_profile)) }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                when (viewData.selectedTab) {
                    MainTab.Today -> TodayScreen()
                    MainTab.Profile -> ProfileScreen()
                }
            }
        }
    }
}
