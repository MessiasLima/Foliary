package dev.appoutlet.foliary.feature.upcoming

import dev.appoutlet.foliary.core.ui.component.task.FoliaryTaskCardViewData
import dev.appoutlet.foliary.core.ui.component.task.fixture

fun UpcomingGroup.Companion.fixture(
    label: UpcomingGroupLabel = UpcomingGroupLabel.Today,
    tasks: List<FoliaryTaskCardViewData> = listOf(FoliaryTaskCardViewData.fixture()),
) = UpcomingGroup(
    label = label,
    tasks = tasks,
)
