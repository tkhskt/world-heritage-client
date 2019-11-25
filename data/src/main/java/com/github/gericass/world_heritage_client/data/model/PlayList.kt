package com.github.gericass.world_heritage_client.data.model

import androidx.room.PrimaryKey

data class PlayList(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String
)