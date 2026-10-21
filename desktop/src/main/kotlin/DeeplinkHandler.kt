import dev.appoutlet.foliary.feature.common.deeplink.Deeplink
import dev.appoutlet.foliary.feature.common.deeplink.getAdditionalQueryParameters
import java.net.URI

internal fun URI.toDeeplink(): Deeplink {
    val regularQueryParameters = this.query?.split('&')?.associate {
        val arg = it.split('=', limit = 2)
        arg[0] to arg.getOrElse(1) { "" }
    } ?: emptyMap()

    val additionalQueryParameters = this.toString().getAdditionalQueryParameters()

    return Deeplink(
        schema = this.scheme ?: "",
        host = this.host ?: "",
        path = this.path ?: "",
        queryParameters = regularQueryParameters + additionalQueryParameters
    )
}
