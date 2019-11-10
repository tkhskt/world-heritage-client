package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.model.Keyword
import com.github.gericass.world_heritage_client.data.model.Videos

interface AvgleRepository {

    suspend fun getCategories(): Categories

    suspend fun getVideoByCategory(page: Int, categoryId: String): Videos

    suspend fun searchVideo(page: Int, keyword: String): Videos

    suspend fun getCollections(page: Int): Collections

    suspend fun insertKeyword(keyword: String)

    suspend fun getAllKeywords(): List<Keyword>

    suspend fun gerSimilarWords(keyword: String): List<Keyword>
}