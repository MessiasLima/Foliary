package dev.appoutlet.foliary.core.database

import androidx.room.RoomDatabase

expect fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>