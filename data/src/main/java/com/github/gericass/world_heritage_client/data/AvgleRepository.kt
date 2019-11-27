package com.github.gericass.world_heritage_client.data

import androidx.annotation.DrawableRes
import com.github.gericass.world_heritage_client.data.model.*

interface AvgleRepository {

    suspend fun getCategories(): Categories

    suspend fun getVideoByCategory(page: Int, categoryId: String): Videos

    suspend fun searchVideo(page: Int, keyword: String): Videos

    suspend fun getCollections(page: Int): Collections

    suspend fun saveKeyword(keyword: String)

    suspend fun getAllKeywords(): List<Keyword>

    suspend fun gerSimilarWords(keyword: String): List<Keyword>

    suspend fun saveInsertHistory(video: Videos.Video)

    suspend fun getViewingHistories(limit: Int = 50): List<ViewingHistory>

    suspend fun getAllPlayList(): List<PlayList>

    suspend fun getPlayListWithVideos(playListId: Int): PlayListWithVideos

    suspend fun savePlayList(
        title: String,
        videos: List<Videos.Video>,
        @DrawableRes
        thumbnail: Int? = null
    )

    suspend fun deletePlayList(playListWithVideos: PlayListWithVideos)

}