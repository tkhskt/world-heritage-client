package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.local.AvgleDatabase
import com.github.gericass.world_heritage_client.data.model.*
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.remote.AvgleClient
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import java.util.*

internal class AvgleRepositoryImpl(
    private val retrofit: Retrofit,
    private val avgleDatabase: AvgleDatabase
) : AvgleRepository {

    private val firestore = Firebase.firestore
    private val client by lazy { retrofit.create(AvgleClient::class.java) }
    private val keywordDao by lazy { avgleDatabase.keywordDao() }
    private val historyDao by lazy { avgleDatabase.viewingHistoryDao() }
    private val playlistDao by lazy { avgleDatabase.playlistDao() }
    private val videoDao by lazy { avgleDatabase.videoDao() }
    private val favoriteVideoDao by lazy { avgleDatabase.favoriteVideoDao() }

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

    override suspend fun savePlaylist(
        title: String,
        description: String,
        videos: List<Videos.Video>,
        thumbnail: Int?
    ) {
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
            Playlist(title = title, description = description, thumbnailImg = this)
        } ?: run {
            Playlist(
                title = title,
                description = description,
                thumbnailImgUrl = videos.first().preview_url
            )
        }
        val id = playlistDao.insertPlaylist(playlist)
        videos.map {
            VideoPlaylist(it.vid, id.toInt())
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

    override suspend fun getFavoriteVideos(limit: Int, offset: Int): List<FavoriteVideo> {
        val videos = mutableListOf<FavoriteVideo>()
        val cachedVideos = favoriteVideoDao.getVideos(limit, offset)
        firestore.collection("favorite")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .startAt(limit * offset)
            .endBefore((limit * offset) + limit)
            .get()
            .addOnSuccessListener {
                videos.addAll(it.map { doc ->
                    doc.toObject(FavoriteVideo::class.java)
                })
            }
            .addOnFailureListener {
                videos.addAll(cachedVideos)
            }.await()
        favoriteVideoDao.insertVideosAfterDelete(videos, limit, offset)
        return videos
    }

    override suspend fun saveFavoriteVideo(videos: List<FavoriteVideo>) {
        videos.forEach {
            firestore.collection("favorite").document(it.vid).set(it).await()
        }
    }

    override suspend fun deleteFavoriteVideo(videoId: String) {
        firestore.collection("favorite").document(videoId).delete().await()
    }
}