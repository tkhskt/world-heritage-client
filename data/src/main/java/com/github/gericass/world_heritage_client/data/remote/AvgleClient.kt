package com.github.gericass.world_heritage_client.data.remote

import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import retrofit2.http.GET
import retrofit2.http.Path

interface AvgleClient {
    @GET(GET_ALL_CATEGORIES)
    suspend fun getAllCategories(): Categories

    @GET(GET_VIDEO_BY_CATEGORY)
    suspend fun getVideoByCategory(
        @Path("page") page: Int,
        @Path("c") categoryId: String
    ): Videos

    @GET(GET_SEARCH_VIDEO)
    suspend fun search(
        @Path("page") page: Int,
        @Path("query") query: String
    ): Videos
}