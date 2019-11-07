package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections

class CollectionViewModel(
    repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    val pagedList: LiveData<PagedList<Collections.Collection>>
    
    private val factory = CollectionDataSourceFactory(viewModelScope, repository)
    val networkState: LiveData<Status> by lazy { factory.getNetworkState() }

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        pagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }
}