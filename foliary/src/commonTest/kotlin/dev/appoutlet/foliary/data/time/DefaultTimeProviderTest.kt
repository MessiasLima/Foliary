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
    fun `should return the start of today for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        subject.startOfToday(timezone) shouldBe Instant.parse("2026-07-22T00:00:00Z")
    }

    @Test
    fun `should return the start of day for the configured time zone`() {
        val timezone = TimeZone.UTC
        val instant = Instant.parse("2026-07-22T15:30:00Z")
        val subject = DefaultTimeProvider()
        subject.startOfDay(instant, timezone) shouldBe Instant.parse("2026-07-22T00:00:00Z")
    }

    @Test
    fun `should return the end of today for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        subject.endOfToday(timezone) shouldBe Instant.parse("2026-07-22T23:59:59.999999999Z")
    }

    @Test
    fun `should return the start of tomorrow for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        subject.startOfTomorrow(timezone) shouldBe Instant.parse("2026-07-23T00:00:00Z")
    }

    @Test
    fun `should return the end of tomorrow for the configured time zone`() {
        val timezone = TimeZone.UTC
        val today = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        subject.endOfTomorrow(timezone) shouldBe Instant.parse("2026-07-23T23:59:59.999999999Z")
    }

    @Test
    fun `should return start and end of tomorrow respecting non-UTC time zone`() {
        val timeZone = TimeZone.of("UTC-10")
        val today = Instant.parse("2026-07-22T02:10:02.999Z") // 2026-07-21 16:10 in UTC-10
        val subject = DefaultTimeProvider(clock = FixedClock(today))
        // Tomorrow in UTC-10 is 2026-07-22
        subject.startOfTomorrow(timeZone) shouldBe Instant.parse("2026-07-22T10:00:00Z")
        subject.endOfTomorrow(timeZone) shouldBe Instant.parse("2026-07-23T09:59:59.999999999Z")
    }

    @Test
    fun `should return the end of day for the configured time zone`() {
        val timezone = TimeZone.UTC
        val instant = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider()
        subject.endOfDay(instant, timezone) shouldBe Instant.parse("2026-07-22T23:59:59.999999999Z")
    }

    @Test
    fun `should return the end of day respecting the time zone`() {
        val instant = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider()
        subject.endOfDay(instant, TimeZone.UTC) shouldBe Instant.parse("2026-07-22T23:59:59.999999999Z")
        subject.endOfDay(instant, TimeZone.of("UTC-10")) shouldBe Instant.parse("2026-07-22T09:59:59.999999999Z")
    }

    @Test
    fun `should format instant to display text for the configured time zone`() {
        val timezone = TimeZone.UTC
        val instant = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider()
        subject.displayText(instant, timezone) shouldBe "22 Jul 2026"
    }

    @Test
    fun `should format instant to display text respecting the time zone`() {
        val instant = Instant.parse("2026-07-22T02:10:02.999Z")
        val subject = DefaultTimeProvider()
        subject.displayText(instant, TimeZone.UTC) shouldBe "22 Jul 2026"
        subject.displayText(instant, TimeZone.of("UTC-10")) shouldBe "21 Jul 2026"
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }
}
