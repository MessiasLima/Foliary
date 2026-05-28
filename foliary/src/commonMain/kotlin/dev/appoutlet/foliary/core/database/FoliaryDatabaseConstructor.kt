package dev.appoutlet.foliary.core.database

import androidx.room3.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object FoliaryDatabaseConstructor : RoomDatabaseConstructor<FoliaryDatabase> {
    override fun initialize(): FoliaryDatabase
}
