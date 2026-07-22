package dev.appoutlet.foliary.core.ui.component.task

import dev.appoutlet.foliary.core.allopen.Open
import dev.appoutlet.foliary.data.task.database.entity.Task
import dev.appoutlet.foliary.data.time.TimeProvider
import org.koin.core.annotation.Single

@Single
@Open
class FoliaryTaskCardViewDataMapper(private val timeProvider: TimeProvider) {
    operator fun invoke(task: Task): FoliaryTaskCardViewData {
        return FoliaryTaskCardViewData(
            id = task.id.toString(),
            title = task.title,
            description = task.description,
            isCompleted = task.completionDate != null,
            isOverdue = task.dueDate?.let { dueDate ->
                dueDate < timeProvider.now()
            } ?: false
        )
    }
}
