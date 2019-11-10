package com.github.gericass.world_heritage_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gericass.world_heritage_client.data.model.Keyword

@Dao
interface KeywordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(period: Keyword)

    @Query("SELECT * FROM Keyword")
    fun getAll(): List<Keyword>

    @Query("SELECT * FROM Keyword WHERE keyword LIKE :keyword || '%'")
    fun getSimilarWords(keyword: String): List<Keyword>

    @Query("DELETE FROM Keyword WHERE ROWID IN (SELECT ROWID FROM Keyword ORDER BY ROWID DESC LIMIT -1 OFFSET 50)")
    fun deleteOldest()
}