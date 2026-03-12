import dev.appoutlet.foliary.core.logging.getLogger
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import io.github.kdroidfilter.nucleus.core.runtime.DeepLinkHandler
import java.net.URI

val log = getLogger("DeeplinkHandler")

fun registerDeeplinkHandler(args: Array<String>) {
    log.d { args.joinToString() }
    DeepLinkHandler.register(args) { uri ->
        log.i { "Received URI: $uri" }
        DeepLinkDispatcher.dispatch(uri.toDeeplink())
    }
}

private fun URI.toDeeplink(): Deeplink {
    val regularQueryParameters = this.query?.split('&')?.associate {
        val (key, value) = it.split('=')
        key to value
    } ?: emptyMap()

    val additionalQueryParameters = this.toString().split('#').getOrNull(1)
        ?.split("&")
        ?.associate {
            val (key, value) = it.split('=')
            key to value
        } ?: emptyMap()

    return Deeplink(
        schema = this.scheme ?: "",
        host = this.host ?: "",
        path = this.path ?: "",
        queryParameters = regularQueryParameters + additionalQueryParameters
    )
}

// TODO handle single instance of application to receive deeplink callback
// TODO store Supabase authentication on database
// TODO check current authentication on start and navigate to main if needed
