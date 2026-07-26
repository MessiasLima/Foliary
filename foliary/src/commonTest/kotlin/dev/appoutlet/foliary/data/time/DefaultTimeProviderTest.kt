package dev.appoutlet.foliary.data.time

import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import io.kotest.matchers.longs.shouldBeAtLeast
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

class DefaultTimeProviderTest {
    @Test
    fun `should return current instant`() {
        val now = Clock.System.now()
        val subject = DefaultTimeProvider()
        subject.now().toEpochMilliseconds() shouldBeAtLeast now.toEpochMilliseconds()
    }

    @Test
    fun `should return the end of today for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        subject.endOfToday(timezone) shouldBe Instant.parse("2026-07-22T23:59:59.999999999Z")
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }
}
