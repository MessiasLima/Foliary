package dev.appoutlet.foliary.data.time

import io.kotest.matchers.longs.shouldBeAtLeast
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

class TimeProviderTest {
    @Test
    fun `should return current instant`() {
        val now = Clock.System.now()
        val subject = TimeProvider()
        subject.now().toEpochMilliseconds() shouldBeAtLeast now.toEpochMilliseconds()
    }

    @Test
    fun `should return the end of today for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = TimeProvider(clock = FixedClock(today))
        subject.endOfToday(timezone) shouldBe Instant.parse("2026-07-22T23:59:59.999999999Z")
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }
}
