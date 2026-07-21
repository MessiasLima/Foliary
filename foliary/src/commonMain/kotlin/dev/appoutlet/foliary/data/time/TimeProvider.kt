package dev.appoutlet.foliary.data.time

import dev.appoutlet.foliary.core.allopen.Open
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Single
import kotlin.time.Clock
import kotlin.time.Instant

@Single
@Open
class TimeProvider(private val clock: Clock = Clock.System) {
    fun now(): Instant = clock.now()

    fun endOfToday(timeZone: TimeZone = TimeZone.currentSystemDefault()): Instant {
        return now().toLocalDateTime(timeZone).date
            .atTime(23, 59, 59, 999999999)
            .toInstant(timeZone)
    }
}
