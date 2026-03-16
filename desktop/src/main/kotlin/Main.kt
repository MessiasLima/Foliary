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
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.logging.initSentry
import java.awt.Dimension

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

// TODO: Create Foliary Website to redirect to when Google Successfull login
// TODO: Use the Foliary website as redirect url for Google
// TODO: Create ticket to enhance the Authenticated screen on Sign In: Use the user name when possible
// TODO: Create ticket to enhance the SignIn Screen, Avoid blinking when opening the app in a already authenticated app

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
