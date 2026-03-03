package dev.appoutlet.foliary.data.local

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>
