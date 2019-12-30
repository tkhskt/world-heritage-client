package com.github.gericass.world_heritage_client.common.vo

data class SpinnerItem(
    val title: String,
    val action: () -> Unit
)