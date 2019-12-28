package com.github.gericass.world_heritage_client.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    primaryKeys = ["video_id", "playlist_id"],
    foreignKeys = [
        ForeignKey(
            entity = VideoEntity::class,
            parentColumns = ["vid"],
            childColumns = ["video_id"]

        ), ForeignKey(
            entity = Playlist::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"],
            onDelete = CASCADE
        )
    ]
)
class VideoPlaylist(
    @ColumnInfo(name = "video_id")
    val videoId: String,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long
)