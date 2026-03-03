package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.foliary.core.logging.logger
import dev.appoutlet.umami.Umami
import dev.appoutlet.umami.api.event

/**
 * Release implementation of Analytics that sends events to Umami analytics service.
 * Used in release builds when analytics configuration is available.
 */
class ReleaseAnalytics(
    private val umami: Umami
) : Analytics {

    private val log by logger()

    init {
        log.i { "Analytics service initialized - tracking enabled" }
    }

    override fun trackScreen(screenName: String, title: String?) {
        log.d { "Track screen: $screenName" }
        umami.event(url = screenName, title = title ?: screenName)
    }

    override fun trackEvent(name: String, data: Map<String, Any>?) {
        log.d { "Track event: $name" }
        umami.event(name = name, data = data)
    }
}
