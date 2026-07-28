package dev.appoutlet.foliary.core.provider.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

private val StartOfDay = LocalTime(0, 0)
private val LastNanosecondOfTheDay = LocalTime.fromNanosecondOfDay((1.days.inWholeNanoseconds - 1))

interface TimeProvider {
    fun now(): Instant
    fun startOfToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant
    fun endOfToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant
    fun endOfDay(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant
    fun displayText(instant: Instant, timeZone: TimeZone = TimeZone.currentSystemDefault()): String
    fun isInThePast(instant: Instant): Boolean
}

@Single
class DefaultTimeProvider(private val clock: Clock = Clock.System) : TimeProvider {
    private val dateFormat = LocalDate.Format {
        day()
        chars(" ")
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        chars(" ")
        year()
    }

    override fun now(): Instant = clock.now()

    override fun startOfToday(timeZone: TimeZone): Instant {
        return now().toLocalDateTime(timeZone).date
            .atTime(StartOfDay)
            .toInstant(timeZone)
    }

    override fun endOfDay(
        instant: Instant,
        timeZone: TimeZone
    ): Instant {
        return instant.toLocalDateTime(timeZone).date
            .atTime(LastNanosecondOfTheDay)
            .toInstant(timeZone)
    }

    override fun endOfToday(timeZone: TimeZone) = endOfDay(now(), timeZone)

    override fun displayText(
        instant: Instant,
        timeZone: TimeZone
    ): String {
        return instant.toLocalDateTime(timeZone).date.format(dateFormat)
    }

    override fun isInThePast(instant: Instant) = instant.toEpochMilliseconds() < now().toEpochMilliseconds()
}
