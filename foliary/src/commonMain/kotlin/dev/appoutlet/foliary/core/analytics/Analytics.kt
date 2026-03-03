package dev.appoutlet.foliary.core.analytics

/**
 * Interface for analytics tracking operations.
 * Provides methods to track screen views, custom events, and user identification.
 */
interface Analytics {
    /**
     * Tracks a screen view.
     *
     * @param screenName The name of the screen being viewed (e.g., "SignInScreen")
     */
    fun trackScreen(screenName: String)

    /**
     * Tracks a custom event.
     *
     * @param name The name of the event in snake_case (e.g., "button_clicked", "app_start")
     * @param data Optional additional data to attach to the event
     */
    fun trackEvent(name: String, data: Map<String, Any>? = null)
}
