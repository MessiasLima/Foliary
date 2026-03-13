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
        handleDeepLinks()
    }


    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLinks()
    }

    private fun handleDeepLinks() {
        val action = intent?.action
        val data = intent?.data

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

        return Deeplink(
            schema = scheme ?: return null,
            host = host ?: return null,
            path = path ?: "",
            queryParameters = queryParameters + additionalQueryParameters
        )
    }
}

