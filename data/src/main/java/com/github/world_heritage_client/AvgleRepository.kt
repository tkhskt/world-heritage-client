package com.github.world_heritage_client

import com.github.world_heritage_client.model.Categories
import com.github.world_heritage_client.model.Videos

interface AvgleRepository {

    fun getCategories(): Categories

    fun getVideoByCategory(categoryId: String): Videos
    
    fun searchVideo(keyword: String): Videos
}