import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.rememberWindowState
import dev.nucleusframework.application.DecoratedWindow
import dev.nucleusframework.application.NucleusApplicationScope
import dev.nucleusframework.application.NucleusDecoratedWindowScope
import dev.nucleusframework.window.TitleBar
import dev.nucleusframework.window.styling.LocalTitleBarStyle

private const val WindowMinimumWidth = 350
private const val WindowMinimumHeight = 900

@Composable
fun NucleusApplicationScope.FoliaryDecoratedWindow(content: @Composable () -> Unit) {
    DecoratedWindow(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 800.dp, height = WindowMinimumHeight.dp),
        title = "Foliary",
        minimumSize = DpSize(WindowMinimumWidth.dp, WindowMinimumHeight.dp),
    ) {
        Box {
            content()
            FoliaryTitleBar()
        }
    }
}

@Composable
private fun NucleusDecoratedWindowScope.FoliaryTitleBar() {
    val defaultStyle = LocalTitleBarStyle.current
    val style = remember {
        defaultStyle.copy(
            colors = defaultStyle.colors.copy(
                background = Color.Transparent,
                inactiveBackground = Color.Transparent,
            )
        )
    }

    TitleBar(style = style)
}
