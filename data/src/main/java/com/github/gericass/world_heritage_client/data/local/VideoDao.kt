package com.github.gericass.world_heritage_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.gericass.world_heritage_client.data.model.VideoEntity

@Dao
interface VideoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: VideoEntity)

    @Query("DELETE FROM VideoEntity WHERE vid = :vid")
    fun deleteVideo(vid: String)
}