package com.github.gericass.world_heritage_client.common

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gericass.world_heritage_client.common.sheet.BottomSheetFragment
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.PlaylistId
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class BaseViewModel(
    private val repository: AvgleRepository
) : ViewModel() {

    var selectedVideo: Videos.Video? = null

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


    fun showPlaylistDialog(fm: FragmentManager) {
        viewModelScope.launch {
            val selectedPlaylistIds = BottomSheetFragment.showWithResult(fm)
            selectedVideo?.let { video ->
                selectedPlaylistIds?.forEach { playlistId ->
                    repository.saveVideoToPlaylist(playlistId, video)
                }
            }
        }
    }
}