package dev.appoutlet.foliary

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import dev.appoutlet.foliary.core.analytics.Analytics
import dev.appoutlet.foliary.core.analytics.LocalAnalytics
import dev.appoutlet.foliary.core.logging.getKoinLogger
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration


@Composable
fun App() {
    KoinApplication(
        configuration = koinConfiguration<FoliaryKoinApplication> {
            logger(getKoinLogger())
        }
    ) {
        val analytics = koinInject<Analytics>()

        // Track app launch
        LaunchedEffect(Unit) {
            analytics.trackEvent("app_start")
        }

        // Provide analytics to all composables
        CompositionLocalProvider(LocalAnalytics provides analytics) {
            FoliaryTheme {
                Navigation()
            }
        }
    }
}
