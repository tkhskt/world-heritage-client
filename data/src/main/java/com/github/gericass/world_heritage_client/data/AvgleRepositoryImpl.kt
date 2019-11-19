package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.local.AvgleDatabase
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.data.model.Keyword
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.data.remote.AvgleClient
import retrofit2.Retrofit
import java.util.*

class AvgleRepositoryImpl(
    private val retrofit: Retrofit,
    private val avgleDatabase: AvgleDatabase
) : AvgleRepository {

    private val client by lazy { retrofit.create(AvgleClient::class.java) }
    private val dao by lazy { avgleDatabase.keywordDao() }

    override suspend fun getCategories(): Categories {
        return client.getAllCategories()
    }

    override suspend fun getVideoByCategory(page: Int, categoryId: String): Videos {
        return client.getVideoByCategory(page, categoryId)
    }

    override suspend fun searchVideo(page: Int, keyword: String): Videos {
        return client.search(page, keyword)
    }

    override suspend fun getCollections(page: Int): Collections {
        return client.getCollections(page)
    }

    override suspend fun insertKeyword(keyword: String) {
        if (keyword.isBlank()) return
        dao.deleteOldest()
        val record = Keyword(keyword, Calendar.getInstance().time)
        dao.insert(record)
    }

    override suspend fun getAllKeywords(): List<Keyword> {
        return dao.getAll()
    }

    override suspend fun gerSimilarWords(keyword: String): List<Keyword> {
        return dao.getSimilarWords(keyword)
    }
}