package com.github.gericass.world_heritage_client.library.playlist

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber


class PlaylistDataSource(
    private val scope: CoroutineScope,
    private val loadingState: MutableLiveData<Status>,
    private val useCase: PlaylistUseCase,
    private val playlistId: Int? = null
) : PageKeyedDataSource<Int, Videos.Video>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Videos.Video>
    ) {
        scope.launch {
            fetch(0) { page, collections ->
                callback.onResult(collections, null, page)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Videos.Video>
    ) {
        scope.launch {
            fetch(params.key) { page, collections ->
                callback.onResult(collections, page)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Videos.Video>
    ) {
        // do nothing
    }

    private suspend fun fetch(
        page: Int,
        callback: (Int?, List<Videos.Video>) -> Unit
    ) {
        if (playlistId == null) return
        try {
            loadingState.postValue(Status.LOADING)
            val videos = useCase.getVideosByPlaylistId(playlistId)
            if (videos.size == 50) {
                val next = page + 1
                callback(next, videos)
            } else {
                callback(null, videos)
            }
            loadingState.postValue(Status.SUCCESS)
        } catch (e: Throwable) {
            Timber.e(e)
            loadingState.postValue(Status.ERROR)
        }
    }
}