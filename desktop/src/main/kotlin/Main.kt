import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.initSentry
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.nucleusframework.application.nucleusApplication

fun main(args: Array<String>) {
    initSentry()
    nucleusApplication(args = args) {
        onDeepLink { uri -> DeepLinkDispatcher.dispatch(uri.toDeeplink()) }
        FoliaryDecoratedWindow { App() }
    }
}
