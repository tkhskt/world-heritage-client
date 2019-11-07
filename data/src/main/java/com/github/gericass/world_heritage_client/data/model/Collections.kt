package com.github.gericass.world_heritage_client.data.model

data class Collections(
    val response: Response,
    val success: Boolean
) {
    data class Response(
        val collections: List<Collection>,
        val current_offset: Int,
        val has_more: Boolean,
        val limit: Int,
        val total_collections: Int
    )

    data class Collection(
        val collection_url: String,
        val cover_url: String,
        val id: String,
        val keyword: String,
        val title: String,
        val total_views: Int,
        val video_count: Int
    )
}
