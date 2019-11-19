package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections

class CollectionViewModel(
    repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    val pagedList: LiveData<PagedList<Collections.Collection>>

    private val _networkStatus = MutableLiveData<Status>()
    val networkStatus: LiveData<Status> = _networkStatus

    private val factory = CollectionDataSourceFactory(viewModelScope, repository, _networkStatus)

    val isRefreshing = MediatorLiveData<Boolean>()

    init {
        val loadingObserver = Observer<Status> {
            if (it == Status.LOADING) {
                return@Observer
            }
            isRefreshing.value = false
        }
        isRefreshing.addSource(_networkStatus, loadingObserver)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        pagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    fun refresh() {
        factory.refresh()
    }
}