package dev.appoutlet.foliary.feature.taskdetail

import dev.appoutlet.foliary.data.task.database.entity.Priority

fun TaskDetailViewData.Loaded.TaskViewData.Companion.fixture(
    title: String = "Water the plants",
    description: String? = "Check the soil moisture first",
    isComplete: Boolean = false,
    dueDate: String? = "29 Jul 2026",
    isOverdue: Boolean = false,
    overduePeriodInDays: Long? = null,
    creationDate: String = "20 Jul 2026",
    completionDate: String? = null,
    priority: Priority = Priority.MEDIUM,
) = TaskDetailViewData.Loaded.TaskViewData(
    title = title,
    description = description,
    isComplete = isComplete,
    dueDate = dueDate,
    isOverdue = isOverdue,
    overduePeriodInDays = overduePeriodInDays,
    creationDate = creationDate,
    completionDate = completionDate,
    priority = priority,
)
