package dev.appoutlet.foliary.core.analytics

/**
 * Mock implementation of Analytics for testing purposes.
 * Captures all analytics calls in lists for verification.
 */
class MockAnalytics : Analytics {
    val screenViews = mutableListOf<Pair<String, String?>>()
    val events = mutableListOf<Pair<String, Map<String, Any>?>>()

    override fun trackScreen(screenName: String, title: String?) {
        screenViews.add(screenName to title)
    }

    override fun trackEvent(name: String, data: Map<String, Any>?) {
        events.add(name to data)
    }

    fun clear() {
        screenViews.clear()
        events.clear()
    }
}
