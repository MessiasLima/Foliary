import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.logging.initSentry
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import io.github.kdroidfilter.nucleus.window.material.MaterialDecoratedWindow
import io.github.kdroidfilter.nucleus.window.material.MaterialTitleBar

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

fun main(args: Array<String>) {
    initSentry()
    registerDeeplinkHandler(args)
    application {
        var restoreRequested by remember { mutableStateOf(false) }
        val log = getLogger("Main")
        manageSingleInstance(
            onRestoreRequest = { requested ->
                restoreRequested = requested
            },
            onSecondInstance = {
                log.i { "Second instance started" }
                return@application
            }
        )

        FoliaryTheme {
            MaterialDecoratedWindow(
                title = "Foliary",
                state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
                onCloseRequest = ::exitApplication,
                minimumSize = DpSize(WindowMinimumWidth.dp, WindowMinimumHeight.dp),
            ) {
                MaterialTitleBar {
                    Row(
                        modifier = Modifier.align(Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Foliary",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }

                LaunchedEffect(restoreRequested) {
                    if (restoreRequested) {
                        window.toFront()
                        restoreRequested = false
                    }
                }

                App()
            }
        }
    }
}