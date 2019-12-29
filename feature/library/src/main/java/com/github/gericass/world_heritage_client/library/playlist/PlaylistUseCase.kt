package com.github.gericass.world_heritage_client.library.playlist

import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.PlaylistId
import com.github.gericass.world_heritage_client.data.model.FavoriteVideo
import com.github.gericass.world_heritage_client.data.model.VideoEntity
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.data.remote.PagingManager

class PlaylistUseCase(
    private val repository: AvgleRepository
) {

    @Suppress("UNCHECKED_CAST")
    suspend fun <T : Any> getVideosByPlaylistId(
        playlistId: Int,
        pagingManager: PagingManager<T>
    ): List<Videos.Video> {
        return when (playlistId) {
            PlaylistId.FAVORITE.id -> repository.getFavoriteVideos(
                pagingManager = (pagingManager as PagingManager<FavoriteVideo>)
            ).map(
                FavoriteVideo::toVideo
            )
            else -> repository.getPlaylistWithVideos(
                playlistId,
                pagingManager = (pagingManager as PagingManager<Videos.Video>)

            ).videos.map(VideoEntity::toVideo)
        }
    }

    suspend fun deleteVideo(playlistId: Int, vid: String) {
        when (playlistId) {
            PlaylistId.FAVORITE.id -> repository.deleteFavoriteVideo(vid)
            else -> {
            }
        }
    }

    fun getPagingManagerByPlaylistId(playlistId: Int): PagingManager<*> {
        return when (playlistId) {
            PlaylistId.FAVORITE.id -> PagingManager(FavoriteVideo::class)
            else -> PagingManager(Videos.Video::class)
        }
    }
}