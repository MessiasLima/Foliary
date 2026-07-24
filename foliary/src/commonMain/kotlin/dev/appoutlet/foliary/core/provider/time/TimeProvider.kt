package dev.appoutlet.foliary.core.provider.time

import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant

private val LastNanosecondOfTheDay = LocalTime.fromNanosecondOfDay((1.days.inWholeNanoseconds - 1))

interface TimeProvider {
    fun now(): Instant
    fun endOfToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant
}

@Single
class DefaultTimeProvider(private val clock: Clock = Clock.System): TimeProvider {
    override fun now(): Instant = clock.now()

    override fun endOfToday(timeZone: TimeZone): Instant {
        return now().toLocalDateTime(timeZone).date
            .atTime(LastNanosecondOfTheDay)
            .toInstant(timeZone)
    }
}

