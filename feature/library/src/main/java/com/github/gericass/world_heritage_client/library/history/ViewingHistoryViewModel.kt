package com.github.gericass.world_heritage_client.library.history

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.ViewingHistory

class ViewingHistoryViewModel(
    repository: AvgleRepository
) : ViewModel() {

    private val _history = MutableLiveData<List<ViewingHistory>>()
    val history: LiveData<List<ViewingHistory>> = _history

    private val _loadingStatus = MutableLiveData<Status>()
    val loadingStatus: LiveData<Status> = _loadingStatus

    private val factory =
        ViewingHistoryDataSourceFactory(viewModelScope, repository, _loadingStatus)


    val isRefreshing = MediatorLiveData<Boolean>()

    val pagedList: LiveData<PagedList<ViewingHistory>>

    var currentKeyword: String? = null

    val keyword = MutableLiveData<String>()

    val searchButton = MutableLiveData<Event<String>>()

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

    fun fetch(keyword: String) {
        currentKeyword = keyword
        factory.setNewKeyword(keyword)
    }

    fun refresh() {
        currentKeyword = null
        keyword.value = null
        factory.setNewKeyword(null)
    }

}