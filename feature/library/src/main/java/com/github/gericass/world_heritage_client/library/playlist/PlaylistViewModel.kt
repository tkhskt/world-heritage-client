package com.github.gericass.world_heritage_client.library.playlist

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.github.gericass.world_heritage_client.data.AvgleRepository

class PlaylistViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    var editable = false
    var playlistId = 0

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {

    }
}