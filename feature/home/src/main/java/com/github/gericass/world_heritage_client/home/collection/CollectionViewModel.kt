package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections

class CollectionViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    lateinit var itemPagedList: LiveData<PagedList<Collections.Collection>>
    lateinit var liveDataSource: LiveData<PageKeyedDataSource<Int, Collections.Collection>>

    init {
        val factory = CollectionDataSourceFactory(viewModelScope, repository)
        liveDataSource = factory.collectionDataSource
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        itemPagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {

    }
}