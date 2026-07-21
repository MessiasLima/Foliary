package dev.appoutlet.foliary.data.task.database.entity

import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant
import kotlin.uuid.Uuid

fun Task.Companion.fixture(
    id: Uuid = Uuid.random(),
    title: String = "Task fixture",
    description: String? = "Task description",
    creationDate: Instant = Clock.System.now(),
    dueDate: Instant? = creationDate.plus(30.days),
    completionDate: Instant? = creationDate.plus(15.days),
    priority: Priority? = Priority.MEDIUM,
    url: String? = "https://foliary.appoutlet.dev/",
    location: Location? = Location.fixture()
) = Task(
    id = id,
    title = title,
    description = description,
    creationDate = creationDate,
    dueDate = dueDate,
    completionDate = completionDate,
    priority = priority,
    url = url,
    location = location
)
