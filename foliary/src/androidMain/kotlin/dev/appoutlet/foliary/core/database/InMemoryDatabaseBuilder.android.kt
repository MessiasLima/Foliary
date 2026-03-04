package dev.appoutlet.foliary.core.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.mp.KoinPlatform

actual fun inMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val context = KoinPlatform.getKoin().get<Context>()
    return Room.inMemoryDatabaseBuilder(context)
}
