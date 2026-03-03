package dev.appoutlet.foliary.core.analytics

import dev.appoutlet.umami.Umami
import dev.appoutlet.umami.util.annotation.InternalUmamiApi
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    @OptIn(InternalUmamiApi::class, ExperimentalUuidApi::class)
    private fun createTestUmami(): Umami {
        return Umami(website = Uuid.random(), false) {
            baseUrl("https://test.umami.dev")
        }
    }
}
