package dev.appoutlet.foliary.core.ui.component.task

fun FoliaryTaskCardViewData.Companion.fixture(
    id: String = "task-id",
    title: String = "Task fixture",
    description: String? = "Task description",
    isCompleted: Boolean = false,
    isOverdue: Boolean = true,
) = FoliaryTaskCardViewData(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    isOverdue = isOverdue,
)
