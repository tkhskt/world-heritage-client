package com.github.gericass.world_heritage_client.common

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gericass.world_heritage_client.common.dialog.sheet.BottomSheetFragment
import com.github.gericass.world_heritage_client.common.dialog.sheet.BottomSheetFragment.Companion.CREATE_PLAYLIST
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.PlaylistId
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class BaseViewModel(
    private val repository: AvgleRepository
) : ViewModel() {

    var selectedVideo: Videos.Video? = null

    private val _showPlaylistTitleDialog = MutableLiveData<Event<Unit>>()
    val showPlaylistTitleDialog: LiveData<Event<Unit>> = _showPlaylistTitleDialog


    fun saveHistory(video: Videos.Video) {
        viewModelScope.launch {
            repository.saveInsertHistory(video)
        }
    }

    fun saveVideoToLater() {
        viewModelScope.launch {
            val video = selectedVideo ?: return@launch
            repository.saveVideoToPlaylist(PlaylistId.LATER.id, video)
        }
    }

    fun createNewPlaylist(title: String, videos: List<Videos.Video>) {
        viewModelScope.launch {
            repository.savePlaylist(title, "", videos)
        }
    }


    fun showPlaylistDialog(fm: FragmentManager) {
        viewModelScope.launch {
            val selectedPlaylistIds = BottomSheetFragment.showWithResult(fm) ?: return@launch
            val video = selectedVideo ?: return@launch
            if (selectedPlaylistIds.size == 1 && selectedPlaylistIds[0] == CREATE_PLAYLIST) {
                _showPlaylistTitleDialog.value = Event(Unit)
                return@launch
            }
            selectedPlaylistIds.forEach { playlistId ->
                repository.saveVideoToPlaylist(playlistId, video)
            }

        }
    }
}