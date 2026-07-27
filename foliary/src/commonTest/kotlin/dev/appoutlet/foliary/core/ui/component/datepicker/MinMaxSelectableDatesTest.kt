package dev.appoutlet.foliary.core.ui.component.datepicker

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.time.Instant

class MinMaxSelectableDatesTest {

    @Test
    fun `should allow any date when using default bounds`() = runTest {
        val subject = MinMaxSelectableDates()

        subject.isSelectableDate(Instant.parse("2026-07-21T00:00:00Z").toEpochMilliseconds()) shouldBe true
    }

    @Test
    fun `should allow date equal to min bound`() = runTest {
        val subject = MinMaxSelectableDates(minDateMillis = 100)

        subject.isSelectableDate(100) shouldBe true
    }

    @Test
    fun `should allow date equal to max bound`() = runTest {
        val subject = MinMaxSelectableDates(maxDateMillis = 100)

        subject.isSelectableDate(100) shouldBe true
    }

    @Test
    fun `should allow date between min and max bounds`() = runTest {
        val subject = MinMaxSelectableDates(minDateMillis = 100, maxDateMillis = 200)

        subject.isSelectableDate(150) shouldBe true
    }

    @Test
    fun `should reject date below min bound`() = runTest {
        val subject = MinMaxSelectableDates(minDateMillis = 100)

        subject.isSelectableDate(99) shouldBe false
    }

    @Test
    fun `should reject date above max bound`() = runTest {
        val subject = MinMaxSelectableDates(maxDateMillis = 100)

        subject.isSelectableDate(101) shouldBe false
    }
}
