package com.github.gericass.world_heritage_client.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


class PlaylistWithVideos {
    @Embedded
    lateinit var playlist: Playlist

    @Relation(
        parentColumn = "id",
        entity = VideoEntity::class,
        entityColumn = "vid",
        associateBy = Junction(
            value = VideoPlaylist::class,
            parentColumn = "playlist_id",
            entityColumn = "video_id"
        )
    )
    lateinit var videos: List<VideoEntity>

    fun toRemoteModel(): PlaylistWithVideosRemoteModel {
        return PlaylistWithVideosRemoteModel(playlist, videos)
    }
}