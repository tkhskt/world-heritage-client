package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.model.Videos

interface AvgleRepository {

    suspend fun getCategories(): Categories

    suspend fun getVideoByCategory(page: Int, categoryId: String): Videos

    suspend fun searchVideo(page: Int, keyword: String): Videos

    suspend fun getCollections(page: Int): Collections
}