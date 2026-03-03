package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.foliary.core.logging.logger

/**
 * Debug implementation of Analytics that only logs events without sending them to any service.
 * Used in debug builds and as a fallback when analytics configuration is missing.
 */
class DebugAnalytics : Analytics {
    private val log by logger()

    override fun trackScreen(screenName: String, title: String?) {
        log.d { "Screen: $screenName, title: $title" }
    }

    override fun trackEvent(name: String, data: Map<String, Any>?) {
        log.d { "Event: $name, data: $data" }
    }

    override fun identifyUser(data: Map<String, Any>) {
        log.d { "Identify: $data" }
    }
}
