import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.kdroid.composetray.tray.api.Tray
import java.awt.Window

@Composable
fun ApplicationScope.FoliaryTray(window: () -> Window) {
    Tray(
        icon = Lucide.Leaf,
        tint = null,
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
