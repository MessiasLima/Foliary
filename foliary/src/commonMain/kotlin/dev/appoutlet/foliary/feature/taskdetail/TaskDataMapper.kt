package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import org.koin.core.annotation.Factory
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailViewData.Loaded.TaskViewData
import kotlin.time.Instant

@Factory
class TaskDataMapper(private val timeProvider: TimeProvider) {
    operator fun invoke(task: Task): TaskViewData {
        val isOverdue = task.dueDate?.isOverdue ?: false

        return TaskViewData(
            title = task.title,
            description = task.description,
            isComplete = task.completionDate != null,
            dueDate = task.dueDate?.displayText,
            isOverdue = isOverdue,
            overduePeriodInDays = task.dueDate?.overduePeriodInDays
        )
    }

    private val Instant.isOverdue: Boolean
        get() = toEpochMilliseconds() < timeProvider.startOfToday().toEpochMilliseconds()


    private val Instant.overduePeriodInDays: Long?
        get() {
            if (isOverdue.not()) return null
            val duration = timeProvider.now() - this
            val durationInDays = duration.inWholeDays
            return durationInDays.takeIf { it > 1 }
        }


    private val Instant.displayText: String
        get() = timeProvider.displayText(this)
}
