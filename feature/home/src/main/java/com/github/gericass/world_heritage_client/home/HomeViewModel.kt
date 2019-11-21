package com.github.gericass.world_heritage_client.home

import androidx.annotation.IdRes
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    var currentPage = 0
    @IdRes
    var currentBottomNavigation: Int? = null
}