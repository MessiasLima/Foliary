package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.fixture

fun UpcomingViewData.TaskGroup.Companion.fixture(
    label: UpcomingViewData.GroupLabel = UpcomingViewData.GroupLabel.Today,
    tasks: List<FoliaryTaskCardViewData> = listOf(FoliaryTaskCardViewData.fixture()),
) = UpcomingViewData.TaskGroup(
    label = label,
    tasks = tasks,
)
