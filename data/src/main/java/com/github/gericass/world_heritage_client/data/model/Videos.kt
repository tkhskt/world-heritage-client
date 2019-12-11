package com.github.gericass.world_heritage_client.data.model

import java.util.*

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
        val viewnumber: Int,
        @Transient
        val createdAt: Date? = null
    ) {
        fun toViewingHistory(date: Date): ViewingHistory {
            return ViewingHistory(
                vid = this.vid,
                addtime = this.addtime,
                channel = this.channel,
                dislikes = this.dislikes,
                duration = this.duration,
                embedded_url = this.embedded_url,
                framerate = this.framerate,
                hd = this.hd,
                keyword = this.keyword,
                likes = this.likes,
                preview_url = this.preview_url,
                title = this.title,
                uid = this.uid,
                video_url = this.video_url,
                viewnumber = this.viewnumber,
                created_at = date
            )
        }

        fun toVideoEntity(date: Date): VideoEntity {
            return VideoEntity(
                vid = this.vid,
                addtime = this.addtime,
                channel = this.channel,
                dislikes = this.dislikes,
                duration = this.duration,
                embedded_url = this.embedded_url,
                framerate = this.framerate,
                hd = this.hd,
                keyword = this.keyword,
                likes = this.likes,
                preview_url = this.preview_url,
                title = this.title,
                uid = this.uid,
                video_url = this.video_url,
                viewnumber = this.viewnumber,
                created_at = date
            )
        }
    }


}
