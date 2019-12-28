package com.github.gericass.world_heritage_client.library.playlist

import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.FavoriteVideo
import com.github.gericass.world_heritage_client.data.model.VideoEntity
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.PlaylistId

class PlaylistUseCase(
    private val repository: AvgleRepository
) {

    suspend fun getVideosByPlaylistId(playlistId: Int): List<Videos.Video> {
        return when (playlistId) {
            PlaylistId.FAVORITE.id -> repository.getFavoriteVideos().map(FavoriteVideo::toVideo)
            else -> repository.getPlaylistWithVideos(playlistId).videos.map(VideoEntity::toVideo)
        }

    }
}