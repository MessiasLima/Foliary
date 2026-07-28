package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.data.task.database.entity.Task
import org.koin.core.annotation.Factory
import dev.appoutlet.foliary.feature.taskdetail.TaskDetailViewData.Loaded.TaskViewData

@Factory
class TaskDataMapper {
    operator fun invoke(task: Task) : TaskViewData {
        return TaskViewData(
            title = task.title
        )
    }
}
