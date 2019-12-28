package com.github.gericass.world_heritage_client.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    val title: String,
    val thumbnailImg: Int? = null,
    val thumbnailImgUrl: String? = null
)