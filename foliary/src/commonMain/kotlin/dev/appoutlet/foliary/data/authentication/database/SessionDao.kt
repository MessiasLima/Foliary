package dev.appoutlet.foliary.data.authentication.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import dev.appoutlet.foliary.data.authentication.model.Session

@Dao
interface SessionDao {
    @Insert
    suspend fun insert(session: Session)

    @Query("DELETE FROM Session")
    suspend fun deleteAll()

    @Query("SELECT * FROM Session LIMIT 1")
    suspend fun load(): Session?
}
