package com.github.gericass.world_heritage_client.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["video_id", "playlist_id"],
    foreignKeys = [
        ForeignKey(
            entity = VideoEntity::class,
            parentColumns = ["vid"],
            childColumns = ["studentId"]
        ), ForeignKey(
            entity = PlayList::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"]
        )
    ]
)
class VideoPlayList(
    @ColumnInfo(name = "video_id")
    val videoId: String,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Int
)