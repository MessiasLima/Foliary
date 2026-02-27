package dev.appoutlet.foliary

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.appoutlet.foliary.core.logging.getKoinLogger
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import org.koin.compose.KoinApplication
import org.koin.plugin.module.dsl.koinConfiguration

@Preview
@Composable
fun App() {
    KoinApplication(configuration = koinConfiguration<FoliaryKoinApplication> {
        logger(getKoinLogger())
    }) {
        FoliaryTheme {
            Navigation()
        }
    }
}
