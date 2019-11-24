package com.github.gericass.world_heritage_client.data.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation


data class PlayListWithVideos(
    @Embedded val playlist: PlayList,
    @Relation(
        parentColumn = "id",
        entity = VideoEntity::class,
        entityColumn = "vid",
        associateBy = Junction(
            value = VideoPlayList::class,
            parentColumn = "playlist_id",
            entityColumn = "video_id"
        )
    )
    val videos: List<VideoEntity>
)