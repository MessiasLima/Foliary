package dev.appoutlet.foliary.data.authentication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import dev.appoutlet.foliary.data.authentication.model.Session

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: Session)

    @Query("DELETE FROM Session")
    suspend fun deleteAll()

    @Query("SELECT * FROM Session LIMIT 1")
    suspend fun load() : Session?
}