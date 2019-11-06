package com.github.gericass.world_heritage_client.data.model

data class Videos(
    val response: Response,
    val success: Boolean
) {
    data class Response(
        val current_offset: Int,
        val has_more: Boolean,
        val limit: Int,
        val total_videos: Int,
        val videos: List<Video>
    )

    data class Video(
        val addtime: Int,
        val channel: String,
        val dislikes: Int,
        val duration: Double,
        val embedded_url: String,
        val framerate: Double,
        val hd: Boolean,
        val keyword: String,
        val likes: Int,
        val preview_url: String,
        val title: String,
        val uid: String,
        val vid: String,
        val video_url: String,
        val viewnumber: Int
    )
}
