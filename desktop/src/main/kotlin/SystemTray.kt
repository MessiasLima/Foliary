import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import com.kdroid.composetray.tray.api.Tray
import foliary.foliary.generated.resources.Res
import foliary.foliary.generated.resources.app_name
import foliary.foliary.generated.resources.ic_foliary_tray
import foliary.foliary.generated.resources.open_app
import foliary.foliary.generated.resources.quit
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Window

@Composable
fun ApplicationScope.FoliaryTray(window: () -> Window) {
    val appName = stringResource(Res.string.app_name)
    val openApp = stringResource(Res.string.open_app)
    val quit = stringResource(Res.string.quit)

    Tray(
        icon = painterResource(Res.drawable.ic_foliary_tray),
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
