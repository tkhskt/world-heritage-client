package com.github.gericass.world_heritage_client.library.history

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ViewingHistoryDataSource(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val loadingState: MutableLiveData<Status>,
    var keyword: String? = null
) : PageKeyedDataSource<Int, ViewingHistory>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, ViewingHistory>
    ) {
        scope.launch {
            fetch(0) { page, collections ->
                callback.onResult(collections, null, page)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ViewingHistory>
    ) {
        scope.launch {
            fetch(params.key) { page, collections ->
                callback.onResult(collections, page)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, ViewingHistory>
    ) {
        // do nothing
    }

    private suspend fun fetch(
        page: Int,
        callback: (Int?, List<ViewingHistory>) -> Unit
    ) {
        try {
            loadingState.postValue(Status.LOADING)
            val history = keyword?.run {
                repository.getViewingHistoriesByKeyword(keyword = this, offset = page)
            } ?: run {
                repository.getViewingHistories(offset = page)
            }
            if (history.size == 50) {
                val next = page + 1
                callback(next, history)
            } else {
                callback(null, history)
            }
            loadingState.postValue(Status.SUCCESS)
        } catch (e: Throwable) {
            Timber.e(e)
            loadingState.postValue(Status.ERROR)
        }
    }
}