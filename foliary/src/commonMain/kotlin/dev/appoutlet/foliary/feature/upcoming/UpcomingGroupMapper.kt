package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.allopen.Open
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.datetime.TimeZone
import org.koin.core.annotation.Single

sealed interface UpcomingGroupLabel {
    data object Overdue : UpcomingGroupLabel
    data object Today : UpcomingGroupLabel
    data object Tomorrow : UpcomingGroupLabel
    data class Date(val displayText: String) : UpcomingGroupLabel
    data object NoDate : UpcomingGroupLabel
}

data class UpcomingGroup(
    val label: UpcomingGroupLabel,
    val tasks: List<FoliaryTaskCardViewData>
) {
    companion object
}

@Single
@Open
class UpcomingGroupMapper(
    private val timeProvider: TimeProvider,
    private val foliaryTaskCardViewDataMapper: FoliaryTaskCardViewDataMapper = FoliaryTaskCardViewDataMapper(
        timeProvider = timeProvider,
    ),
) {
    operator fun invoke(
        tasks: List<Task>,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): List<UpcomingGroup> {
        val startOfToday = timeProvider.startOfToday(timeZone)
        val endOfToday = timeProvider.endOfToday(timeZone)
        val startOfTomorrow = timeProvider.startOfTomorrow(timeZone)
        val endOfTomorrow = timeProvider.endOfTomorrow(timeZone)

        val overdueTasks = mutableListOf<Task>()
        val todayTasks = mutableListOf<Task>()
        val tomorrowTasks = mutableListOf<Task>()
        val futureTasksByDay = mutableMapOf<kotlin.time.Instant, MutableList<Task>>()
        val noDateTasks = mutableListOf<Task>()

        for (task in tasks) {
            val dueDate = task.dueDate
            when {
                dueDate == null -> noDateTasks.add(task)
                dueDate < startOfToday -> overdueTasks.add(task)
                dueDate in startOfToday..endOfToday -> todayTasks.add(task)
                dueDate in startOfTomorrow..endOfTomorrow -> tomorrowTasks.add(task)
                else -> {
                    val dayStart = timeProvider.startOfDay(dueDate, timeZone)
                    futureTasksByDay.getOrPut(dayStart) { mutableListOf() }.add(task)
                }
            }
        }

        val taskComparator = compareByDescending<Task> { it.priority }
            .thenBy { it.dueDate }
            .thenBy { it.title }

        fun sortAndMap(list: List<Task>): List<FoliaryTaskCardViewData> {
            return list.sortedWith(taskComparator).map { foliaryTaskCardViewDataMapper(it) }
        }

        val groups = mutableListOf<UpcomingGroup>()

        if (overdueTasks.isNotEmpty()) {
            groups.add(UpcomingGroup(UpcomingGroupLabel.Overdue, sortAndMap(overdueTasks)))
        }
        if (todayTasks.isNotEmpty()) {
            groups.add(UpcomingGroup(UpcomingGroupLabel.Today, sortAndMap(todayTasks)))
        }
        if (tomorrowTasks.isNotEmpty()) {
            groups.add(UpcomingGroup(UpcomingGroupLabel.Tomorrow, sortAndMap(tomorrowTasks)))
        }

        val sortedFutureDays = futureTasksByDay.keys.sorted()
        for (dayStart in sortedFutureDays) {
            val dayTasks = futureTasksByDay.getValue(dayStart)
            val displayText = timeProvider.displayText(dayStart, timeZone)
            groups.add(UpcomingGroup(UpcomingGroupLabel.Date(displayText), sortAndMap(dayTasks)))
        }

        if (noDateTasks.isNotEmpty()) {
            groups.add(UpcomingGroup(UpcomingGroupLabel.NoDate, sortAndMap(noDateTasks)))
        }

        return groups
    }
}
