package dev.appoutlet.foliary.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.koin.mp.KoinPlatform

actual fun getDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase> {
    val context = KoinPlatform.getKoin().get<Context>()
    val dbFile = context.getDatabasePath("foliary.db")
    return Room.databaseBuilder<FoliaryDatabase>(
        context = context,
        name = dbFile.absolutePath,
        factory = { FoliaryDatabaseConstructor.initialize() }
    )
}

actual fun getInMemoryDatabaseBuilder(): RoomDatabase.Builder<FoliaryDatabase>? {
    val koin = runCatching { KoinPlatform.getKoin() }.getOrNull()
    val context = koin?.getOrNull<Context>()
    return context?.let {
        Room.inMemoryDatabaseBuilder<FoliaryDatabase>(
            context = it,
            factory = { FoliaryDatabaseConstructor.initialize() }
        )
    }
}
