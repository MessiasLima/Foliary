package dev.appoutlet.foliary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.logging.getKoinLogger
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import org.koin.compose.KoinApplication
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration

@Composable
fun App() {
    KoinApplication(
        configuration = koinConfiguration<FoliaryKoinApplication> {
            logger(getKoinLogger())
        }
    ) {
        val analytics = LocalAnalytics.current

        // Track app launch
        LaunchedEffect(Unit) {
            analytics.trackEvent("app_start")
        }

        FoliaryTheme { Navigation() }
    }
}
