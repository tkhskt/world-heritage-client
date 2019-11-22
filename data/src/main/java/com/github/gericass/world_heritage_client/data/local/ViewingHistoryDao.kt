package com.github.gericass.world_heritage_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gericass.world_heritage_client.data.model.ViewingHistory

@Dao
interface ViewingHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(history: ViewingHistory)

    @Query("SELECT * FROM ViewingHistory ORDER BY created_at DESC LIMIT :limit")
    suspend fun getHistories(limit: Int): List<ViewingHistory>

    @Query("DELETE FROM ViewingHistory WHERE ROWID IN (SELECT ROWID FROM ViewingHistory ORDER BY ROWID DESC LIMIT -1 OFFSET 100)")
    suspend fun deleteOldest()
}