import dev.appoutlet.foliary.core.logging.getLogger
import io.github.kdroidfilter.nucleus.core.runtime.DeepLinkHandler
import javax.swing.JOptionPane

val log = getLogger("DeeplinkHandler")

fun registerDeeplinkHandler(args: Array<String>) {
    DeepLinkHandler.register(args) { uri ->
        log.d { "Received URI: $uri" }
        println(uri.toString())
        JOptionPane.showInputDialog("Received URI: $uri")
    }
}