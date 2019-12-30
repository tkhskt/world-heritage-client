package com.github.gericass.world_heritage_client.data

enum class PlaylistId(val id: Int, val title: String, val collectionName: String) {
    HISTORY(-1, "履歴", "history"),
    FAVORITE(-2, "お気に入り", "favorite"),
    LATER(-3, "後で見る", "later")
}