package com.github.gericass.world_heritage_client.data.local

import androidx.room.*
import com.github.gericass.world_heritage_client.data.model.LaterVideo

@Dao
abstract class LaterVideoDao {

    @Query("SELECT * FROM LaterVideo ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    abstract suspend fun getVideos(limit: Int, offset: Int): List<LaterVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVideo(video: LaterVideo)

    @Query("DELETE FROM LaterVideo WHERE vid IN (SELECT vid FROM (SELECT vid FROM LaterVideo ORDER BY created_at DESC LIMIT :limit OFFSET :offset) AS dummy)")
    abstract fun deleteVideos(limit: Int, offset: Int)

    @Transaction
    open suspend fun insertVideosAfterDelete(video: List<LaterVideo>, limit: Int, offset: Int) {
        deleteVideos(limit, offset)
        video.forEach {
            insertVideo(it)
        }
    }
}