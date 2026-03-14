import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import io.github.kdroidfilter.nucleus.core.runtime.DeepLinkHandler
import java.net.URI

fun registerDeeplinkHandler(args: Array<String>) {
    DeepLinkHandler.register(args) { uri ->
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
