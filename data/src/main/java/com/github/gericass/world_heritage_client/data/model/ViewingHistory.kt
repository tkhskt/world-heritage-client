package com.github.gericass.world_heritage_client.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class ViewingHistory(
    @PrimaryKey
    val vid: String,
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
    val video_url: String,
    val viewnumber: Int,
    val created_at: Date
) : Serializable {

    fun toVideo(): Videos.Video {
        return Videos.Video(
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
            createdAt = this.created_at
        )
    }

}