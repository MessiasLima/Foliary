package dev.appoutlet.foliary.core.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect")
expect object FoliaryDatabaseConstructor : RoomDatabaseConstructor<FoliaryDatabase> {
    override fun initialize(): FoliaryDatabase
}