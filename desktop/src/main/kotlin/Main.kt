import androidx.compose.ui.window.application
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.initSentry

fun main(args: Array<String>) {
    initSentry()
    registerDeeplinkHandler(args)
    application {
        DecoratedWindow { App() }
    }
}
