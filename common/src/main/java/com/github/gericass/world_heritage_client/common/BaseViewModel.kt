package com.github.gericass.world_heritage_client.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class BaseViewModel(
    private val repository: AvgleRepository
) : ViewModel() {

    fun saveHistory(video: Videos.Video) {
        viewModelScope.launch {
            repository.saveInsertHistory(video)
        }
    }
}