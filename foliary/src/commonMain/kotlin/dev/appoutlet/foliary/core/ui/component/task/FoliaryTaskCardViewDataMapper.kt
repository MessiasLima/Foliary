package dev.appoutlet.foliary.core.ui.component.task

import dev.appoutlet.foliary.core.allopen.Open
import dev.appoutlet.foliary.core.provider.time.TimeProvider
import dev.appoutlet.foliary.data.task.database.entity.Task
import org.koin.core.annotation.Single
import kotlin.time.Instant

@Single
@Open
class FoliaryTaskCardViewDataMapper(private val timeProvider: TimeProvider) {
    operator fun invoke(task: Task): FoliaryTaskCardViewData {
        return FoliaryTaskCardViewData(
            id = task.id.toString(),
            title = task.title,
            description = task.description,
            isCompleted = task.completionDate != null,
            isOverdue = mapIsOverdue(
                dueDate = task.dueDate,
                completionDate = task.completionDate,
            )
        )
    }

    private fun mapIsOverdue(dueDate: Instant?, completionDate: Instant?): Boolean {
        val now = timeProvider.now()
        return when {
            completionDate != null -> false
            dueDate == null -> false
            dueDate < now -> true
            else -> false
        }
    }
}
