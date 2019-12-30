package com.github.gericass.world_heritage_client.data.model

data class PlaylistWithVideosRemoteModel(
    var playlist: Playlist = Playlist(title = ""),
    var videos: List<VideoEntity> = emptyList()
) {
    fun toPlaylistWithVideos(): PlaylistWithVideos {
        return PlaylistWithVideos().apply {
            playlist = this@PlaylistWithVideosRemoteModel.playlist
            videos = this@PlaylistWithVideosRemoteModel.videos
        }
    }
}