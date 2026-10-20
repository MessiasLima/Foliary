package dev.appoutlet.foliary.feature.createtask

import dev.appoutlet.foliary.data.task.database.entity.Priority

fun CreateTaskViewData.Companion.fixture(
    id: String? = null,
    title: String = "Task fixture",
    description: String? = "Task description",
    url: String = "",
    isUrlInvalid: Boolean = false,
    priority: Priority = Priority.MEDIUM,
    dueDate: CreateTaskViewData.DueDateViewData? = CreateTaskViewData.DueDateViewData.fixture(),
    minDueDateMillis: Long = 1_752_960_000_000,
    saveButtonEnabled: Boolean = true,
) = CreateTaskViewData(
    id = id,
    title = title,
    description = description,
    url = url,
    isUrlInvalid = isUrlInvalid,
    priority = priority,
    dueDate = dueDate,
    minDueDateMillis = minDueDateMillis,
    saveButtonEnabled = saveButtonEnabled,
)

fun CreateTaskViewData.DueDateViewData.Companion.fixture(
    selectedDateMillis: Long = 1_752_960_000_000,
    selectedDateDisplayText: String = "21 Jul 2026",
) = CreateTaskViewData.DueDateViewData(
    selectedDateMillis = selectedDateMillis,
    selectedDateDisplayText = selectedDateDisplayText,
)
