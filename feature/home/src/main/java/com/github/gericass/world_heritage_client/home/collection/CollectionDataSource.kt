package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CollectionDataSource(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository
) : PageKeyedDataSource<Int, Collections.Collection>() {


    private val _networkState: MutableLiveData<Status> = MutableLiveData()
    val networkState: LiveData<Status> = _networkState

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Collections.Collection>
    ) {
        scope.launch {
            fetch(0) { page, collections ->
                callback.onResult(collections, null, page)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Collections.Collection>
    ) {
        scope.launch {
            fetch(params.key) { page, collections ->
                callback.onResult(collections, page)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, Collections.Collection>
    ) {
        // do nothing
    }

    private suspend fun fetch(
        page: Int,
        callback: (Int?, List<Collections.Collection>) -> Unit
    ) {
        try {
            _networkState.postValue(Status.LOADING)
            val collections = withContext(Dispatchers.IO) {
                repository.getCollections(page)
            }
            if (collections.response.has_more) {
                val next = page + 1
                callback(next, collections.response.collections)
            } else {
                callback(null, collections.response.collections)
            }
            _networkState.postValue(Status.SUCCESS)
        } catch (e: Throwable) {
            Timber.e(e)
            _networkState.postValue(Status.ERROR)
        }
    }

}