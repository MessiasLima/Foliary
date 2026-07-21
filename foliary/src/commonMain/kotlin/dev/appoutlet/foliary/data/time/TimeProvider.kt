package dev.appoutlet.foliary.data.time

import dev.appoutlet.foliary.core.allopen.Open
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

@Single
@Open
class TimeProvider(private val clock: Clock = Clock.System) {
    fun now(): Instant = clock.now()

    fun endOfToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant {
        return now().toLocalDateTime(timeZone).date
            .atTime(LastNanosecondOfTheDay)
            .toInstant(timeZone)
    }
}
