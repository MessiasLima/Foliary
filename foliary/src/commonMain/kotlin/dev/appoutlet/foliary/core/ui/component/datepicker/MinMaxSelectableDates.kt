package dev.appoutlet.foliary.core.ui.component.datepicker

import androidx.compose.material3.SelectableDates
import kotlin.time.Instant

class MinMaxSelectableDates(
    private val minDateMillis: Long = Instant.DISTANT_PAST.toEpochMilliseconds(),
    private val maxDateMillis: Long = Instant.DISTANT_FUTURE.toEpochMilliseconds(),
) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        return utcTimeMillis in minDateMillis..maxDateMillis
    }
}
