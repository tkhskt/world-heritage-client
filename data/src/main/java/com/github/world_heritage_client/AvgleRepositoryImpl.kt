package com.github.world_heritage_client

import com.github.world_heritage_client.model.Categories
import com.github.world_heritage_client.model.Videos
import com.github.world_heritage_client.remote.AvgleClient
import retrofit2.Retrofit

class AvgleRepositoryImpl(
        private val retrofit: Retrofit
) : AvgleRepository {

    private val client by lazy { retrofit.create(AvgleClient::class.java) }

    override fun getCategories(): Categories {
    }

    override fun getVideoByCategory(categoryId: String): Videos {
    }

    override fun searchVideo(keyword: String): Videos {
    }
}