package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.provider.time.DefaultTimeProvider
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.database.entity.Priority
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.task.database.entity.fixture
import io.kotest.matchers.shouldBe
import kotlin.test.Test
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

class UpcomingViewDataMapperTest {
    private val clockTime = Instant.parse("2026-06-05T10:00:00Z")
    private val timeProvider = DefaultTimeProvider(clock = FixedClock(clockTime))
    private val cardMapper = FoliaryTaskCardViewDataMapper(timeProvider)
    private val mapper = UpcomingViewDataMapper(timeProvider, cardMapper)

    @Test
    fun `should return Empty when there are no tasks`() {
        mapper(emptyList()) shouldBe UpcomingViewData.Empty
    }

    @Test
    fun `should group tasks by due date bucket in first encounter order`() {
        val today = timeProvider.startOfToday()
        val tomorrow = timeProvider.startOfTomorrow()
        val overdueDate = today - 1.hours
        val todayDate = today + 12.hours
        val tomorrowDate = tomorrow + 12.hours
        val futureDate1 = tomorrow + 2.days + 12.hours
        val futureDate2 = tomorrow + 3.days + 12.hours

        val overdueTask = Task.fixture(title = "Overdue task", dueDate = overdueDate, completionDate = null)
        val todayTask = Task.fixture(title = "Today task", dueDate = todayDate, completionDate = null)
        val tomorrowTask = Task.fixture(title = "Tomorrow task", dueDate = tomorrowDate, completionDate = null)
        val futureTask1 = Task.fixture(title = "Future task 1", dueDate = futureDate1, completionDate = null)
        val futureTask2 = Task.fixture(title = "Future task 2", dueDate = futureDate2, completionDate = null)
        val noDateTask = Task.fixture(title = "No date task", dueDate = null, completionDate = null)

        val viewData = mapper(
            listOf(
                noDateTask,
                futureTask2,
                overdueTask,
                futureTask1,
                todayTask,
                tomorrowTask,
            )
        )

        viewData shouldBe UpcomingViewData.Loaded(
            listOf(
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.NoDate,
                    tasks = listOf(cardMapper(noDateTask)),
                ),
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Date(timeProvider.displayText(futureDate2)),
                    tasks = listOf(cardMapper(futureTask2)),
                ),
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Overdue,
                    tasks = listOf(cardMapper(overdueTask)),
                ),
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Date(timeProvider.displayText(futureDate1)),
                    tasks = listOf(cardMapper(futureTask1)),
                ),
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Today,
                    tasks = listOf(cardMapper(todayTask)),
                ),
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Tomorrow,
                    tasks = listOf(cardMapper(tomorrowTask)),
                ),
            )
        )
    }

    @Test
    fun `should sort tasks within a group from highest to lowest priority`() {
        val todayDate = timeProvider.startOfToday() + 12.hours

        val taskLow = Task.fixture(
            title = "Z Low Priority",
            priority = Priority.LOW,
            dueDate = todayDate,
            completionDate = null,
        )
        val taskBlocker = Task.fixture(
            title = "A Blocker Priority",
            priority = Priority.BLOCKER,
            dueDate = todayDate,
            completionDate = null,
        )
        val taskHighestFirst = Task.fixture(
            title = "B Highest Priority First",
            priority = Priority.HIGHEST,
            dueDate = todayDate,
            completionDate = null,
        )
        val taskHighestSecond = Task.fixture(
            title = "C Highest Priority Second",
            priority = Priority.HIGHEST,
            dueDate = todayDate,
            completionDate = null,
        )
        val taskHighestThird = Task.fixture(
            title = "A Highest Priority Third",
            priority = Priority.HIGHEST,
            dueDate = todayDate,
            completionDate = null,
        )
        val taskHighestFourth = Task.fixture(
            title = "D Highest Priority Fourth",
            priority = Priority.HIGHEST,
            dueDate = todayDate,
            completionDate = null,
        )

        val viewData = mapper(
            listOf(
                taskLow,
                taskHighestFirst,
                taskBlocker,
                taskHighestSecond,
                taskHighestThird,
                taskHighestFourth,
            )
        )

        viewData shouldBe UpcomingViewData.Loaded(
            listOf(
                UpcomingViewData.TaskGroup(
                    label = UpcomingViewData.GroupLabel.Today,
                    tasks = listOf(
                        taskBlocker,
                        taskHighestFirst,
                        taskHighestSecond,
                        taskHighestThird,
                        taskHighestFourth,
                        taskLow,
                    ).map { cardMapper(it) },
                )
            )
        )
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }
}
