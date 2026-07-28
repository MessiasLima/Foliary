package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import org.koin.core.annotation.Factory
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailViewData.Loaded.TaskViewData
import kotlin.time.Instant

@Factory
class TaskDataMapper(private val timeProvider: TimeProvider) {
    operator fun invoke(task: Task): TaskViewData {
        return TaskViewData(
            title = task.title,
            description = task.description,
            isComplete = task.completionDate != null,
            dueDate = task.dueDate?.displayText,
            isOverdue = task.dueDate?.isOverdue ?: false,
            overduePeriodInDays = task.dueDate?.overduePeriodInDays,
            creationDate = task.creationDate.displayText,
            completionDate = task.completionDate?.displayText
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
