package com.github.gericass.world_heritage_client.data

import androidx.annotation.DrawableRes
import com.github.gericass.world_heritage_client.data.model.*
import com.github.gericass.world_heritage_client.data.remote.PagingManager

interface AvgleRepository {

    suspend fun getCategories(): Categories

    suspend fun getVideoByCategory(page: Int, categoryId: String): Videos

    suspend fun searchVideo(page: Int, keyword: String): Videos

    suspend fun getCollections(page: Int): Collections

    suspend fun saveKeyword(keyword: String)

    suspend fun getAllKeywords(): List<Keyword>

    suspend fun gerSimilarWords(keyword: String): List<Keyword>

    suspend fun saveInsertHistory(video: Videos.Video)

    suspend fun getViewingHistories(limit: Int = 50, offset: Int = 0): List<ViewingHistory>

    suspend fun getViewingHistoriesByKeyword(
        keyword: String,
        limit: Int = 50,
        offset: Int = 0
    ): List<ViewingHistory>

    suspend fun getAllPlaylist(): List<Playlist>

    suspend fun getPlaylistWithVideos(
        playlistId: Int
    ): PlaylistWithVideos

    suspend fun savePlaylist(
        title: String,
        description: String,
        videos: List<Videos.Video>,
        @DrawableRes
        thumbnail: Int? = null
    )

    suspend fun deletePlaylist(playlistId: Int)

    suspend fun getFavoriteVideos(
        limit: Int = 50,
        offset: Int = 0,
        pagingManager: PagingManager<FavoriteVideo>
    ): List<FavoriteVideo>

    suspend fun saveFavoriteVideo(videos: List<FavoriteVideo>)

    suspend fun deleteFavoriteVideo(videoId: String)

    suspend fun saveVideoToPlaylist(
        playlistId: Int,
        video: Videos.Video
    )

    suspend fun deleteVideoFromPlaylist(
        playlistId: Int,
        video: Videos.Video
    )

    suspend fun getLaterVideos(
        limit: Int = 50,
        offset: Int = 0,
        pagingManager: PagingManager<LaterVideo>
    ): List<LaterVideo>

    suspend fun saveLaterVideo(videos: List<LaterVideo>)

    suspend fun deleteLaterVideo(videoId: String)


}