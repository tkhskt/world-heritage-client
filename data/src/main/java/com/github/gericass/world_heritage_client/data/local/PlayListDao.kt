package com.github.gericass.world_heritage_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.gericass.world_heritage_client.data.model.PlayList
import com.github.gericass.world_heritage_client.data.model.PlayListWithVideos
import com.github.gericass.world_heritage_client.data.model.VideoPlayList

@Dao
interface PlayListDao {

    @Transaction
    @Query("SELECT * FROM PlayList WHERE id = :playListId")
    suspend fun getPlayList(playListId: Int): PlayListWithVideos

    @Query("SELECT * FROM PlayList")
    suspend fun getAllPlayList(): List<PlayList>

    @Insert
    suspend fun insertPlayList(playList: PlayList): Long

    @Insert
    suspend fun insertVideoPlayList(videoPlayList: VideoPlayList)

    @Query("DELETE FROM PlayList WHERE id = :playListId")
    fun deletePlayList(playListId: Long)

    @Query("DELETE FROM VideoPlayList WHERE playlist_id = :playListId")
    fun deletePlayListVideo(playListId: Long)

    @Query("SELECT COUNT(*) FROM VideoPlayList WHERE video_id = :videoId")
    fun videoExists(videoId: String): Long
}