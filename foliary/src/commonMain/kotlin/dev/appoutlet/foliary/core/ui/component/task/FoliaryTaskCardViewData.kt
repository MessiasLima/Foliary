package dev.appoutlet.foliary.core.ui.component.task

data class FoliaryTaskCardViewData(
    val id: String,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val isOverdue: Boolean,
)
