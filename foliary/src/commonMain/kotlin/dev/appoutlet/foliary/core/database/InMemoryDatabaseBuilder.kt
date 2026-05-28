package dev.appoutlet.foliary.core.database

import androidx.room3.RoomDatabase

expect fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>
