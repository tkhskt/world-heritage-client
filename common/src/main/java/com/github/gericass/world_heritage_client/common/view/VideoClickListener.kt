package com.github.gericass.world_heritage_client.common.view

import com.github.gericass.world_heritage_client.data.model.Videos

interface VideoClickListener {
    fun onClick(video: Videos.Video)
}