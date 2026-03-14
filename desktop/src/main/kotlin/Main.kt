import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.initSentry
import java.awt.Dimension

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

fun main(args: Array<String>) {
    initSentry()
    registerDeeplinkHandler(args)
    application {
        var restoreRequested by remember { mutableStateOf(false) }

        manageSingleInstance(
            onRestoreRequest = { requested ->
                restoreRequested = requested
            },
            onSecondInstance = {
                return@application
            }
        )

        Window(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(WindowMinimumWidth, WindowMinimumHeight)

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
