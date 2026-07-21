package dev.appoutlet.foliary.data.task.database.entity

import kotlin.random.Random

fun Location.Companion.fixture(
    latitude: Double = Random.nextDouble(-90.0, 90.0),
    longitude: Double = Random.nextDouble(-180.0, 180.0),
) = Location(
    latitude = latitude,
    longitude = longitude
)
