import androidx.compose.ui.window.ComposeUIViewController
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.initSentry
import platform.UIKit.UIViewController

// TODO: Add sentry dependency to xcode project

@Suppress("FunctionNaming", "UNUSED")
fun MainViewController(): UIViewController {
    initSentry()
    return ComposeUIViewController {
        App()
    }
}
