package com.github.gericass.world_heritage_client.library.playlist.show

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val useCase: PlaylistUseCase
) : ViewModel(), LifecycleObserver {

    var editable = false

    var playlistId = 0
        set(value) {
            field = value
            factory.playListId = value
        }

    var title = ""
    var description = ""

    private val _videos = MutableLiveData<List<Videos.Video>>()
    val videos: LiveData<List<Videos.Video>> = _videos

    private val _loadingStatus = MutableLiveData<Status>()
    val loadingStatus: LiveData<Status> = _loadingStatus

    private val factory =
        PlaylistDataSourceFactory(
            viewModelScope,
            useCase,
            _loadingStatus
        )

    val isRefreshing = MediatorLiveData<Boolean>()

    val pagedList: LiveData<PagedList<Videos.Video>>

    init {
        val loadingObserver = Observer<Status> {
            if (it == Status.LOADING) {
                return@Observer
            }
            isRefreshing.value = false
        }
        isRefreshing.addSource(_loadingStatus, loadingObserver)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        pagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    fun deleteVideo(video: Videos.Video) {
        viewModelScope.launch {
            useCase.deleteVideo(playlistId, video.vid)
        }
    }

    fun refresh() {
        factory.refresh()
    }
}