package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Instant

class UpcomingGroupMapperTest {
    private val clockTime = Instant.parse("2026-06-05T10:00:00Z")
    private val timeProvider = DefaultTimeProvider(clock = FixedClock(clockTime))
    private val cardMapper = FoliaryTaskCardViewDataMapper(timeProvider)
    private val mapper = UpcomingGroupMapper(timeProvider, cardMapper)

    @Test
    fun `should bucket tasks into correct groups in chronological order and skip empty groups`() {
        val overdueTask = Task.fixture(
            title = "Overdue task",
            dueDate = Instant.parse("2026-06-04T08:00:00Z"),
        )
        val todayTask = Task.fixture(
            title = "Today task",
            dueDate = Instant.parse("2026-06-05T15:00:00Z"),
        )
        val tomorrowTask = Task.fixture(
            title = "Tomorrow task",
            dueDate = Instant.parse("2026-06-06T12:00:00Z"),
        )
        val futureTask1 = Task.fixture(
            title = "Future task 1",
            dueDate = Instant.parse("2026-06-07T09:00:00Z"),
        )
        val futureTask2 = Task.fixture(
            title = "Future task 2",
            dueDate = Instant.parse("2026-06-08T10:00:00Z"),
        )
        val noDateTask = Task.fixture(
            title = "No date task",
            dueDate = null,
        )

        val groups = mapper(
            listOf(
                noDateTask,
                futureTask2,
                overdueTask,
                futureTask1,
                todayTask,
                tomorrowTask,
            ),
            timeZone = TimeZone.UTC
        )

        groups shouldHaveSize 6
        groups[0].label shouldBe UpcomingGroupLabel.Overdue
        groups[0].tasks.map { it.id } shouldBe listOf(overdueTask.id.toString())

        groups[1].label shouldBe UpcomingGroupLabel.Today
        groups[1].tasks.map { it.id } shouldBe listOf(todayTask.id.toString())

        groups[2].label shouldBe UpcomingGroupLabel.Tomorrow
        groups[2].tasks.map { it.id } shouldBe listOf(tomorrowTask.id.toString())

        groups[3].label shouldBe UpcomingGroupLabel.Date("07 Jun 2026")
        groups[3].tasks.map { it.id } shouldBe listOf(futureTask1.id.toString())

        groups[4].label shouldBe UpcomingGroupLabel.Date("08 Jun 2026")
        groups[4].tasks.map { it.id } shouldBe listOf(futureTask2.id.toString())

        groups[5].label shouldBe UpcomingGroupLabel.NoDate
        groups[5].tasks.map { it.id } shouldBe listOf(noDateTask.id.toString())
    }

    @Test
    fun `should sort tasks within a group by priority descending then due date ascending then title ascending`() {
        val date = Instant.parse("2026-06-05T12:00:00Z")

        val taskLow = Task.fixture(
            title = "Z Low Priority",
            priority = Priority.LOW,
            dueDate = date,
        )
        val taskBlocker = Task.fixture(
            title = "A Blocker Priority",
            priority = Priority.BLOCKER,
            dueDate = date,
        )
        val taskHighestEarlier = Task.fixture(
            title = "B Highest Priority Earlier",
            priority = Priority.HIGHEST,
            dueDate = Instant.parse("2026-06-05T08:00:00Z"),
        )
        val taskHighestLater = Task.fixture(
            title = "A Highest Priority Later",
            priority = Priority.HIGHEST,
            dueDate = Instant.parse("2026-06-05T18:00:00Z"),
        )
        val taskHighestSameTimeA = Task.fixture(
            title = "Alpha",
            priority = Priority.HIGHEST,
            dueDate = date,
        )
        val taskHighestSameTimeB = Task.fixture(
            title = "Beta",
            priority = Priority.HIGHEST,
            dueDate = date,
        )

        val groups = mapper(
            listOf(
                taskLow,
                taskHighestSameTimeB,
                taskBlocker,
                taskHighestLater,
                taskHighestSameTimeA,
                taskHighestEarlier,
            ),
            timeZone = TimeZone.UTC
        )

        groups shouldHaveSize 1
        val group = groups[0]
        group.label shouldBe UpcomingGroupLabel.Today

        val expectedIds = listOf(
            taskBlocker.id.toString(),
            taskHighestEarlier.id.toString(),
            taskHighestSameTimeA.id.toString(),
            taskHighestSameTimeB.id.toString(),
            taskHighestLater.id.toString(),
            taskLow.id.toString(),
        )

        group.tasks.map { it.id } shouldBe expectedIds
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }
}
