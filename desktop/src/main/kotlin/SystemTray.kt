import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import com.composables.icons.lucide.Leaf
import com.composables.icons.lucide.Lucide
import com.kdroid.composetray.tray.api.Tray

@Composable
fun ApplicationScope.FoliaryTray(
    onBringToFront: () -> Unit
) {
    Tray(
        icon = Lucide.Leaf,
        tint = null,
        tooltip = "Foliary",
        primaryAction = onBringToFront
    ) {
        Item(label = "Bring to Front") { onBringToFront() }
        Divider()
        Item(label = "Quit") { exitApplication() }
    }
}
