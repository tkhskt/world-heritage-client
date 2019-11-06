package com.github.gericass.world_heritage_client.data

import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.data.remote.AvgleClient
import retrofit2.Retrofit

class AvgleRepositoryImpl(
    private val retrofit: Retrofit
) : AvgleRepository {

    private val client by lazy { retrofit.create(AvgleClient::class.java) }

    override suspend fun getCategories(): Categories {
        return client.getAllCategories()
    }

    override suspend fun getVideoByCategory(page: Int, categoryId: String): Videos {
        return client.getVideoByCategory(page, categoryId)
    }

    override suspend fun searchVideo(page: Int, keyword: String): Videos {
        return client.search(page, keyword)
    }
}