package com.github.gericass.world_heritage_client.common.dialog.sheet

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.common.R
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.PlaylistId
import com.github.gericass.world_heritage_client.data.model.Playlist
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> = _playlists

    val checkedList = mutableListOf<Int>()

    val buttonChannel = Channel<List<Int>>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        viewModelScope.launch {
            val allPlaylist = repository.getAllPlaylist().toMutableList()
            val favorite = Playlist(
                PlaylistId.FAVORITE.id,
                PlaylistId.FAVORITE.title,
                "",
                R.drawable.common_ic_favorite_24dp
            )
            allPlaylist.add(0, favorite)
            _playlists.value = allPlaylist
        }
    }

    fun onDoneClick() {
        viewModelScope.launch {
            buttonChannel.send(checkedList)
        }
    }
}