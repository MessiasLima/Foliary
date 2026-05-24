import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.logging.initSentry
import java.awt.Dimension
import java.awt.Window

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

fun main(args: Array<String>) {
    initSentry()
    registerDeeplinkHandler(args)
    application {
        var restoreRequested by remember { mutableStateOf(false) }
        var appWindow by remember { mutableStateOf<Window?>(null) }
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

        FoliaryTray(onBringToFront = { restoreRequested = true })

        Window(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = {
                appWindow?.isVisible = false
            },
        ) {
            window.minimumSize = Dimension(WindowMinimumWidth, WindowMinimumHeight)

            SideEffect { appWindow = window }

            LaunchedEffect(restoreRequested) {
                if (restoreRequested) {
                    window.isVisible = true
                    window.toFront()
                    restoreRequested = false
                }
            }

            App()
        }
    }
}