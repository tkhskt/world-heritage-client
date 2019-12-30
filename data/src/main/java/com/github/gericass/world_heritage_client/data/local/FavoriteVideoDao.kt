package com.github.gericass.world_heritage_client.data.local

import androidx.room.*
import com.github.gericass.world_heritage_client.data.model.FavoriteVideo

@Dao
abstract class FavoriteVideoDao {

    @Query("SELECT * FROM FavoriteVideo ORDER BY created_at DESC LIMIT :limit OFFSET :offset")
    abstract suspend fun getVideos(limit: Int, offset: Int): List<FavoriteVideo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVideo(video: FavoriteVideo)

    @Query("DELETE FROM FavoriteVideo WHERE vid IN (SELECT vid FROM (SELECT vid FROM FavoriteVideo ORDER BY created_at DESC LIMIT :limit OFFSET :offset) AS dummy)")
    abstract fun deleteVideos(limit: Int, offset: Int)

    @Transaction
    open suspend fun insertVideosAfterDelete(video: List<FavoriteVideo>, limit: Int, offset: Int) {
        deleteVideos(limit, offset)
        video.forEach {
            insertVideo(it)
        }
    }
}