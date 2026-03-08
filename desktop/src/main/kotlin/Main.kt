import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.initSentry
import java.awt.Dimension

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

fun main() {
    initSentry()
    application {
        Window(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = ::exitApplication,
        ) {
            window.minimumSize = Dimension(WindowMinimumWidth, WindowMinimumHeight)
            App()
        }
    }
}
