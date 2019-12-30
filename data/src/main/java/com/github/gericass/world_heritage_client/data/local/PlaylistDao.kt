package com.github.gericass.world_heritage_client.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.github.gericass.world_heritage_client.data.model.PlaylistWithVideos
import com.github.gericass.world_heritage_client.data.model.VideoPlaylist

@Dao
interface PlaylistDao {

    @Transaction
    @Query("SELECT * FROM Playlist WHERE id = :playlistId")
    suspend fun getPlaylist(playlistId: Int): PlaylistWithVideos

    @Query("SELECT * FROM Playlist")
    suspend fun getAllPlaylist(): List<Playlist>

    @Insert
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Insert
    suspend fun insertVideoPlaylist(videoPlaylist: VideoPlaylist)

    @Query("DELETE FROM Playlist WHERE id = :playlistId")
    fun deletePlaylist(playlistId: Int)

    @Query("DELETE FROM VideoPlaylist WHERE playlist_id = :playlistId")
    fun deletePlaylistVideo(playlistId: Int)

    @Query("SELECT COUNT(*) FROM VideoPlaylist WHERE video_id = :videoId")
    fun videoExists(videoId: String): Int
}