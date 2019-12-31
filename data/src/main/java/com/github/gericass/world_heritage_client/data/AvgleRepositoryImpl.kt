package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.local.AvgleDatabase
import com.github.gericass.world_heritage_client.data.model.*
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.remote.AvgleClient
import com.github.gericass.world_heritage_client.data.remote.PagingManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import java.util.*
import javax.security.auth.login.LoginException

internal class AvgleRepositoryImpl(
    private val retrofit: Retrofit,
    private val avgleDatabase: AvgleDatabase
) : AvgleRepository {

    private val firestore = Firebase.firestore
    private val userId by lazy { FirebaseAuth.getInstance().currentUser?.uid }
    private val client by lazy { retrofit.create(AvgleClient::class.java) }
    private val keywordDao by lazy { avgleDatabase.keywordDao() }
    private val historyDao by lazy { avgleDatabase.viewingHistoryDao() }
    private val playlistDao by lazy { avgleDatabase.playlistDao() }
    private val videoDao by lazy { avgleDatabase.videoDao() }
    private val favoriteVideoDao by lazy { avgleDatabase.favoriteVideoDao() }
    private val laterVideoDao by lazy { avgleDatabase.lateVideoDao() }

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
        var playlists = playlistDao.getAllPlaylist()
        val user = userId ?: throw LoginException("user not logged in")
        firestore
            .collection(user)
            .document("playlist")
            .collection("playlist")
            .get()
            .addOnSuccessListener {
                playlists = it.map { queryDocumentSnapshot ->
                    queryDocumentSnapshot.toObject(PlaylistWithVideosRemoteModel::class.java)
                        .playlist
                }
            }.await()
        return playlists
    }

    override suspend fun getPlaylistWithVideos(playlistId: Int): PlaylistWithVideos {
        var playlistWithVideos = playlistDao.getPlaylist(playlistId)
        val user = userId ?: throw LoginException("user not logged in")
        firestore
            .collection(user)
            .document("playlist")
            .collection("playlist")
            .document(playlistId.toString())
            .get()
            .addOnSuccessListener {
                it.toObject(PlaylistWithVideosRemoteModel::class.java)?.toPlaylistWithVideos()
                    ?.let { pv ->
                        playlistWithVideos = pv
                    }
            }.await()
        playlistDao.insertPlaylist(playlistWithVideos.playlist)
        playlistWithVideos.videos.forEach {
            videoDao.insertVideo(it)
            val vp = VideoPlaylist(it.vid, playlistId)
            playlistDao.insertVideoPlaylist(vp)
        }
        return playlistWithVideos
    }

    override suspend fun savePlaylist(
        title: String,
        description: String,
        videos: List<Videos.Video>,
        thumbnail: Int?
    ) {
        if (videos.isNullOrEmpty()) return
        val date = Calendar.getInstance().time
        val user = userId ?: throw LoginException("user not logged in")
        videos.map {
            it.toVideoEntity(date)
        }.forEach {
            videoDao.insertVideo(it)
        }
        val playlistId = (1..Int.MAX_VALUE).random()
        val playlist = thumbnail?.run {
            Playlist(title = title, description = description, thumbnailImg = this)
        } ?: run {
            Playlist(
                id = playlistId,
                title = title,
                description = description,
                thumbnailImgUrl = videos.first().preview_url
            )
        }
        playlistDao.insertPlaylist(playlist)
        videos.map {
            VideoPlaylist(it.vid, playlistId)
        }.forEach {
            playlistDao.insertVideoPlaylist(it)
        }
        firestore
            .collection(user)
            .document("playlist")
            .collection("playlist")
            .document(playlistId.toString())
            .set(PlaylistWithVideosRemoteModel(playlist, videos.map { it.toVideoEntity(date) }))
            .await()
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        val playlistWithVideos = playlistDao.getPlaylist(playlistId)
        val user = userId ?: throw LoginException("user not logged in")
        withContext(Dispatchers.IO) {
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
        firestore.collection(user)
            .document("playlist")
            .collection("playlist")
            .document(playlistWithVideos.playlist.id.toString())
            .delete()
            .await()
    }

    override suspend fun getFavoriteVideos(
        limit: Int,
        offset: Int,
        pagingManager: PagingManager<FavoriteVideo>
    ): List<FavoriteVideo> {
        val videos = mutableListOf<FavoriteVideo>()
        val cachedVideos = favoriteVideoDao.getVideos(limit, offset)
        pagingManager.collectionPath = PlaylistId.FAVORITE.collectionName
        pagingManager.getRecords(
            onSuccess = {
                videos.addAll(it)
            },
            onFailure = {
                videos.addAll(cachedVideos)
            }
        )
        favoriteVideoDao.insertVideosAfterDelete(videos, limit, offset)
        return videos
    }

    override suspend fun saveFavoriteVideo(videos: List<FavoriteVideo>) {
        val user = userId ?: throw LoginException("user not logged in")
        videos.forEach {
            firestore.collection(user)
                .document(PlaylistId.FAVORITE.collectionName)
                .collection(PlaylistId.FAVORITE.collectionName)
                .document(it.vid).set(it)
                .await()
        }
    }

    override suspend fun deleteFavoriteVideo(videoId: String) {
        val user = userId ?: throw LoginException("user not logged in")
        firestore.collection(user)
            .document(PlaylistId.FAVORITE.collectionName)
            .collection(PlaylistId.FAVORITE.collectionName)
            .document(videoId).delete()
            .await()
    }

    override suspend fun saveVideoToPlaylist(playlistId: Int, video: Videos.Video) {
        val now = Calendar.getInstance().time
        val user = userId ?: throw LoginException("user not logged in")
        when (playlistId) {
            PlaylistId.FAVORITE.id -> saveFavoriteVideo(listOf(video.toFavoriteVideo(now)))
            PlaylistId.LATER.id -> saveLaterVideo(listOf(video.toLaterVideo(now)))
            else -> firestore.collection(user)
                .document("playlist")
                .collection("playlist")
                .document(playlistId.toString())
                .update("videos", FieldValue.arrayUnion(video.toVideoEntity()))
        }
    }

    override suspend fun deleteVideoFromPlaylist(playlistId: Int, video: Videos.Video) {
        val user = userId ?: throw LoginException("user not logged in")
        when (playlistId) {
            PlaylistId.FAVORITE.id -> deleteFavoriteVideo(video.vid)
            PlaylistId.LATER.id -> deleteLaterVideo(video.vid)
            else -> firestore.collection(user)
                .document("playlist")
                .collection("playlist")
                .document(playlistId.toString())
                .update("videos", FieldValue.arrayRemove(video.toVideoEntity()))
        }
    }

    override suspend fun getLaterVideos(
        limit: Int,
        offset: Int,
        pagingManager: PagingManager<LaterVideo>
    ): List<LaterVideo> {
        val videos = mutableListOf<LaterVideo>()
        val cachedVideos = laterVideoDao.getVideos(limit, offset)
        pagingManager.collectionPath = PlaylistId.LATER.collectionName
        pagingManager.getRecords(
            onSuccess = {
                videos.addAll(it)
            },
            onFailure = {
                videos.addAll(cachedVideos)
            }
        )
        laterVideoDao.insertVideosAfterDelete(videos, limit, offset)
        return videos
    }

    override suspend fun saveLaterVideo(videos: List<LaterVideo>) {
        val user = userId ?: throw LoginException("user not logged in")
        videos.forEach {
            firestore.collection(user)
                .document(PlaylistId.LATER.collectionName)
                .collection(PlaylistId.LATER.collectionName)
                .document(it.vid).set(it)
                .await()
        }
    }

    override suspend fun deleteLaterVideo(videoId: String) {
        val user = userId ?: throw LoginException("user not logged in")
        firestore.collection(user)
            .document(PlaylistId.LATER.collectionName)
            .collection(PlaylistId.LATER.collectionName)
            .document(videoId).delete()
            .await()
    }
}