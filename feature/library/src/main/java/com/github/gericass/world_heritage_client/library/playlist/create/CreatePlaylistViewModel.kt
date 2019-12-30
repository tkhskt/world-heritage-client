package com.github.gericass.world_heritage_client.library.playlist.create

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(
    private val repository: AvgleRepository
) : ViewModel() {

    val selectedVideos = mutableListOf<Videos.Video>()

    val selectedItemCount = MutableLiveData<Int>()

    private val _video = MutableLiveData<List<Videos.Video>>()
    val video: LiveData<List<Videos.Video>> = _video

    private val _loadingStatus = MutableLiveData<Status>()
    val loadingStatus: LiveData<Status> = _loadingStatus

    private val factory =
        CreatePlaylistDataSouceFactory(viewModelScope, repository, _loadingStatus)

    val isRefreshing = MediatorLiveData<Boolean>()

    val pagedList: LiveData<PagedList<Videos.Video>>

    val keyword = MutableLiveData<String>()

    init {
        val loadingObserver = Observer<Status> {
            if (it == Status.LOADING) {
                return@Observer
            }
            isRefreshing.value = false
        }
        selectedItemCount.value = 0
        isRefreshing.addSource(_loadingStatus, loadingObserver)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        pagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    fun createNewPlaylist(title: String) {
        viewModelScope.launch {
            repository.savePlaylist(title, "", selectedVideos)
        }
    }

    fun refresh() {
        factory.refresh()
    }
}