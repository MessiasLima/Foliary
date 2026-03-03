package dev.appoutlet.foliary.core.analytics

/**
 * Mock implementation of Analytics for testing purposes.
 * Captures all analytics calls in lists for verification.
 */
class MockAnalytics : Analytics {
    val screenViews: List<String>
        field = mutableListOf<String>()

    val events: Map<String, Map<String, Any>?>
        field = mutableMapOf<String, Map<String, Any>?>()

    override fun trackScreen(screenName: String) {
        screenViews.add(screenName)
    }

    override fun trackEvent(name: String, data: Map<String, Any>?) {
        events[name] = data
    }

    fun clear() {
        screenViews.clear()
        events.clear()
    }
}
