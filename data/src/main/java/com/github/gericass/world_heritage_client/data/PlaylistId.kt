package com.github.gericass.world_heritage_client.data

enum class PlaylistId(val id: Int, val title: String) {
    HISTORY(-1, "履歴"),
    FAVORITE(-2, "お気に入り"),
    LATER(-3, "後で見る")
}