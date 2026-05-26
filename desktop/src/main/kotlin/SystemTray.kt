import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.ApplicationScope
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.kdroid.composetray.tray.api.Tray
import com.kdroid.composetray.utils.isMenuBarInDarkMode
import java.awt.Window

// TODO: Create string resources (add rule on readme)
// TODO: Replace Lucide icon by the application icon
@Composable
fun ApplicationScope.FoliaryTray(window: () -> Window) {
    Tray(
        icon = Lucide.Leaf,
        tint = if (isMenuBarInDarkMode()) Color.White else Color.Black,
        tooltip = "Foliary",
        primaryAction = { toggleWindow(window()) }
    ) {
        Item(label = "Open Foliary") { bringToFront(window()) }
        Divider()
        Item(label = "Quit") { exitApplication() }
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
