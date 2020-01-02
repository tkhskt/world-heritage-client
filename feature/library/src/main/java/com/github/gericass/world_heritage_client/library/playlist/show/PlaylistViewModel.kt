package com.github.gericass.world_heritage_client.library.playlist.show

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val repository: AvgleRepository,
    private val useCase: PlaylistUseCase
) : ViewModel(), LifecycleObserver {

    var editable = false

    var playlistId = 0
        set(value) {
            field = value
            factory.playListId = value
        }

    private val _videos = MutableLiveData<List<Videos.Video>>()
    val videos: LiveData<List<Videos.Video>> = _videos

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

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

    var deleteProgress = MutableLiveData<Boolean>()

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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        viewModelScope.launch {
            runCatching {
                useCase.getPlaylist(playlistId)
            }.onSuccess {
                _loadingStatus.value = Status.SUCCESS
                _playlist.value = it
            }.onFailure {
                _loadingStatus.value = Status.ERROR
            }
        }
    }

    fun deleteVideo(video: Videos.Video) {
        viewModelScope.launch {
            deleteProgress.value = true
            runCatching {
                useCase.deleteVideo(playlistId, video)
            }.also {
                deleteProgress.value = false
            }
        }
    }

    fun deletePlaylist(onDelete: () -> Unit) {
        viewModelScope.launch {
            deleteProgress.value = true
            runCatching {
                repository.deletePlaylist(playlistId)
            }.onSuccess {
                onDelete()
            }.also {
                deleteProgress.value = false
            }

        }
    }

    fun refresh() {
        init()
        factory.refresh()
    }
}