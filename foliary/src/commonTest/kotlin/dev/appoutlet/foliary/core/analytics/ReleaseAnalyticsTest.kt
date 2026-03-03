package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.umami.Umami
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test

class ReleaseAnalyticsTest {

    @Test
    fun `should initialize without throwing`() {
        // Given a valid Umami instance
        val umami = createTestUmami()

        // When service is created
        val service = ReleaseAnalytics(umami)

        // Then no exception is thrown
        service shouldNotBe null
    }

    @Test
    fun `should track events without throwing`() {
        // Given analytics service
        val umami = createTestUmami()
        val service = ReleaseAnalytics(umami)

        // When tracking various events
        // Then no exception is thrown
        service.trackEvent("test_event")
        service.trackScreen("TestScreen")
    }

    private fun createTestUmami(): Umami {
        return Umami(website = "00000000-0000-0000-0000-000000000000") {
            baseUrl("https://test.umami.dev")
        }
    }
}
