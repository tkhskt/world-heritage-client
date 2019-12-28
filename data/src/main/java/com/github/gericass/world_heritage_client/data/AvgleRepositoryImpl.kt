package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.local.AvgleDatabase
import com.github.gericass.world_heritage_client.data.model.*
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.remote.AvgleClient
import retrofit2.Retrofit
import java.util.*

internal class AvgleRepositoryImpl(
    private val retrofit: Retrofit,
    private val avgleDatabase: AvgleDatabase
) : AvgleRepository {

    private val client by lazy { retrofit.create(AvgleClient::class.java) }
    private val keywordDao by lazy { avgleDatabase.keywordDao() }
    private val historyDao by lazy { avgleDatabase.viewingHistoryDao() }
    private val playlistDao by lazy { avgleDatabase.playlistDao() }
    private val videoDao by lazy { avgleDatabase.videoDao() }

    override suspend fun getCategories(): Categories {
        return client.getAllCategories()
    }

    override suspend fun getVideoByCategory(page: Int, categoryId: String): Videos {
        return client.getVideoByCategory(page, categoryId)
    }

    override suspend fun searchVideo(page: Int, keyword: String): Videos {
        return client.search(page, keyword)
    }

    override suspend fun getCollections(page: Int): Collections {
        return client.getCollections(page)
    }

    override suspend fun saveKeyword(keyword: String) {
        if (keyword.isBlank()) return
        keywordDao.deleteOldest()
        val record = Keyword(keyword, Calendar.getInstance().time)
        keywordDao.insert(record)
    }

    override suspend fun getAllKeywords(): List<Keyword> {
        return keywordDao.getAll()
    }

    override suspend fun gerSimilarWords(keyword: String): List<Keyword> {
        return keywordDao.getSimilarWords(keyword)
    }

    override suspend fun saveInsertHistory(video: Videos.Video) {
        historyDao.deleteOldest()
        val history = video.toViewingHistory(Calendar.getInstance().time)
        historyDao.insert(history)
    }

    override suspend fun getViewingHistories(limit: Int, offset: Int): List<ViewingHistory> {
        return historyDao.getHistories(limit, offset)
    }

    override suspend fun getViewingHistoriesByKeyword(
        keyword: String,
        limit: Int,
        offset: Int
    ): List<ViewingHistory> {
        return historyDao.getHistoriesByKeyword(keyword, limit, offset)
    }

    override suspend fun getAllPlaylist(): List<Playlist> {
        return playlistDao.getAllPlaylist()
    }

    override suspend fun getPlaylistWithVideos(playlistId: Int): PlaylistWithVideos {
        return playlistDao.getPlaylist(playlistId)
    }

    override suspend fun savePlaylist(title: String, videos: List<Videos.Video>, thumbnail: Int?) {
        if (videos.isNullOrEmpty()) return
        val date = Calendar.getInstance().time
        videos.run {
            map {
                it.toVideoEntity(date)
            }.forEach {
                videoDao.insertVideo(it)
            }
        }
        val playlist = thumbnail?.run {
            Playlist(title = title, thumbnailImg = this)
        } ?: run {
            Playlist(title = title, thumbnailImgUrl = videos.first().preview_url)
        }
        val id = playlistDao.insertPlaylist(playlist)
        videos.map {
            VideoPlaylist(it.vid, id)
        }.forEach {
            playlistDao.insertVideoPlaylist(it)
        }
    }

    override suspend fun deletePlaylist(playlistWithVideos: PlaylistWithVideos) {
        avgleDatabase.runInTransaction {
            playlistDao.run {
                deletePlaylist(playlistWithVideos.playlist.id)
                deletePlaylistVideo(playlistWithVideos.playlist.id)
            }
            playlistWithVideos.videos.forEach {
                val count = playlistDao.videoExists(it.vid)
                if (count < 1) {
                    videoDao.deleteVideo(it.vid)
                }
            }
        }
    }
}