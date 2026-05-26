import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import io.github.kdroidfilter.nucleus.window.DecoratedWindowScope
import io.github.kdroidfilter.nucleus.window.material.MaterialDecoratedWindow
import io.github.kdroidfilter.nucleus.window.material.MaterialTitleBar
import io.github.kdroidfilter.nucleus.window.styling.LocalTitleBarStyle
import java.awt.Window

private val log = getLogger("Decorated Window")

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

@Composable
fun ApplicationScope.DecoratedWindow(content: @Composable () -> Unit) {
    var restoreRequested by remember { mutableStateOf(false) }
    var appWindow by remember { mutableStateOf<Window?>(null) }

    manageSingleInstance(
        onRestoreRequest = { requested ->
            restoreRequested = requested
        },
        onSecondInstance = {
            log.i { "Second instance started" }
            return@DecoratedWindow
        }
    )

    appWindow?.let { window ->
        FoliaryTray({ window })
    }

    FoliaryTheme {
        MaterialDecoratedWindow(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = {
                appWindow?.isVisible = false
            },
            minimumSize = DpSize(WindowMinimumWidth.dp, WindowMinimumHeight.dp),
        ) {
            SideEffect { appWindow = window }

            LaunchedEffect(restoreRequested) {
                if (restoreRequested) {
                    window.isVisible = true
                    window.toFront()
                    restoreRequested = false
                }
            }

            Box {
                content()
                TitleBar()
            }
        }
    }
}

@Composable
private fun DecoratedWindowScope.TitleBar() {
    val defaultStyle = LocalTitleBarStyle.current
    val style = remember {
        defaultStyle.copy(
            colors = defaultStyle.colors.copy(
                background = Color.Transparent,
                inactiveBackground = Color.Transparent,
            )
        )
    }

    MaterialTitleBar(style = style)
}
