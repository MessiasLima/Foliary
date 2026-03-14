package dev.appoutlet.foliary.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.appoutlet.foliary.App
import dev.appoutlet.foliary.core.logging.logger
import dev.appoutlet.foliary.feature.common.deeplink.DeepLinkDispatcher
import dev.appoutlet.foliary.feature.common.deeplink.Deeplink

class MainActivity : ComponentActivity() {
    private val log by logger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { App() }
        handleDeepLinks(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLinks(intent)
    }

    private fun handleDeepLinks(intent: Intent) {
        val action = intent.action
        val data = intent.data

        if (action != Intent.ACTION_VIEW) return
        if (data == null) return

        data.toDeeplink()?.let { deeplink ->
            DeepLinkDispatcher.dispatch(deeplink)
        }
    }

    private fun Uri.toDeeplink(): Deeplink? {
        val queryParameters = this.queryParameterNames.associateWith { this.getQueryParameter(it) ?: "" }
        val additionalQueryParameters = this.toString().split('#').getOrNull(1)
            ?.split("&")
            ?.associate {
                val (key, value) = it.split('=')
                key to value
            } ?: emptyMap()

        if (scheme.isNullOrBlank() || host.isNullOrBlank()) {
            log.w { "Invalid deeplink: missing scheme or host. URI: $this" }
            return null
        }

        return Deeplink(
            schema = scheme ?: "",
            host = host ?: "",
            path = path ?: "",
            queryParameters = queryParameters + additionalQueryParameters
        )
    }
}
