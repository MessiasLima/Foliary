import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import dev.appoutlet.foliary.core.ui.theme.FoliaryTheme
import dev.nucleusframework.application.NucleusApplicationScope
import dev.nucleusframework.application.NucleusDecoratedWindowScope
import dev.nucleusframework.window.material.MaterialDecoratedWindow
import dev.nucleusframework.window.material.MaterialTitleBar
import dev.nucleusframework.window.styling.LocalTitleBarStyle

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

@Composable
fun NucleusApplicationScope.DecoratedWindow(content: @Composable () -> Unit) {
    FoliaryTheme {
        MaterialDecoratedWindow(
            title = "Foliary",
            state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
            onCloseRequest = { exitApplication() },
            minimumSize = DpSize(WindowMinimumWidth.dp, WindowMinimumHeight.dp),
        ) {
            Box {
                content()
                TitleBar()
            }
        }
    }
}

@Composable
private fun NucleusDecoratedWindowScope.TitleBar() {
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
