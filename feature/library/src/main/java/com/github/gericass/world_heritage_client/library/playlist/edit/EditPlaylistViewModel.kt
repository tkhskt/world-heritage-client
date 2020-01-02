package com.github.gericass.world_heritage_client.library.playlist.edit

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Playlist
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    var playlistId: Int = 0

    val titleEditText = MutableLiveData<String>()
    val descriptionEditText = MutableLiveData<String>()

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    var thumbnail = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    private val _update = MutableLiveData<Event<Status>>()
    val update: LiveData<Event<Status>> = _update


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun getPlaylist() {
        viewModelScope.launch {
            loading.value = true
            runCatching {
                repository.getPlaylistWithVideos(playlistId)
            }.onSuccess {
                _playlist.value = it.playlist
                _status.value = Status.SUCCESS
            }.onFailure {
                _status.value = Status.ERROR
            }.also {
                loading.value = false
            }
        }
    }

    fun updatePlaylistInfo() {
        viewModelScope.launch {
            if (titleEditText.value.isNullOrEmpty()) return@launch
            loading.value = true
            val title = titleEditText.value ?: ""
            val description = descriptionEditText.value ?: ""
            runCatching {
                repository.updatePlaylist(playlistId, title, description)
            }.onSuccess {
                _update.value = Event(Status.SUCCESS)
            }.onFailure {
                _update.value = Event(Status.ERROR)
            }.also {
                loading.value = false
            }
        }
    }
}