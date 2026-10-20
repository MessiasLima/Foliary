package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.allopen.Open
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewDataMapper
import dev.appoutlet.foliary.data.task.database.entity.Task
import kotlinx.datetime.TimeZone
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Single
import kotlin.time.Instant

@Factory
@Open
class UpcomingViewDataMapper(
    private val timeProvider: TimeProvider,
    private val foliaryTaskCardViewDataMapper: FoliaryTaskCardViewDataMapper
) {
    operator fun invoke(tasks: List<Task>): UpcomingViewData {
        return when {
            tasks.isEmpty() -> UpcomingViewData.Empty
            else -> mapLoadedState(tasks)
        }
    }

    private fun mapLoadedState(tasks: List<Task>): UpcomingViewData.Loaded {
        val taskGroups = tasks
            .groupBy { task -> mapGroupLabel(task) }
            .mapValues { (groupLabel, tasks) ->
                tasks.sortedBy { it.priority }.map(foliaryTaskCardViewDataMapper::invoke)
            }
            .map { (groupLabel, tasks) -> UpcomingViewData.TaskGroup(groupLabel, tasks) }
            .toList()

        return UpcomingViewData.Loaded(taskGroups)
    }

    private fun mapGroupLabel(task: Task) : UpcomingViewData.GroupLabel {
        return when {
            task.dueDate == null -> UpcomingViewData.GroupLabel.NoDate
            task.dueDate.isOverdue() -> UpcomingViewData.GroupLabel.Overdue
            task.dueDate.isToday() -> UpcomingViewData.GroupLabel.Today
            task.dueDate.isTomorrow() -> UpcomingViewData.GroupLabel.Tomorrow
            else -> {
                val displayText = timeProvider.displayText(task.dueDate)
                UpcomingViewData.GroupLabel.Date(displayText)
            }
        }
    }

    private fun Instant.isOverdue(): Boolean {
        return this < timeProvider.startOfToday()
    }

    private fun Instant.isToday(): Boolean {
        return this > timeProvider.startOfToday() &&
            this < timeProvider.endOfToday()
    }

    private fun Instant.isTomorrow(): Boolean {
        return this > timeProvider.startOfTomorrow() &&
            this < timeProvider.endOfTomorrow()
    }
}
