import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.kdroid.composetray.tray.api.Tray
import com.kdroid.composetray.utils.isMenuBarInDarkMode
import java.awt.Window
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.app_name
import foliary.foliary.generated.resources.open_app
import foliary.foliary.generated.resources.quit
import org.jetbrains.compose.resources.stringResource

// TODO: Replace Lucide icon by the application icon
@Composable
fun ApplicationScope.FoliaryTray(window: () -> Window) {
    val appName = stringResource(Res.string.app_name)
    val openApp = stringResource(Res.string.open_app)
    val quit = stringResource(Res.string.quit)

    Tray(
        icon = Lucide.Leaf,
        tint = if (isMenuBarInDarkMode()) Color.White else Color.Black,
        tooltip = appName,
        primaryAction = { toggleWindow(window()) }
    ) {
        Item(label = openApp) { bringToFront(window()) }
        Divider()
        Item(label = quit) { exitApplication() }
    }
}

private fun toggleWindow(window: Window) {
    window.isVisible = window.isVisible.not()
    if (window.isVisible) {
        window.toFront()
    }
}

private fun bringToFront(window: Window) {
    window.isVisible = true
    window.toFront()
}
