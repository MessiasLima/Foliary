import androidx.compose.ui.window.ComposeUIViewController
import dev.appoutlet.foliary.App
import platform.UIKit.UIViewController

@Suppress("FunctionNaming", "UNUSED")
fun MainViewController(): UIViewController = ComposeUIViewController { 
    App()
}