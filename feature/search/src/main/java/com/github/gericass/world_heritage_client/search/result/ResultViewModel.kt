package com.github.gericass.world_heritage_client.search.result

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class ResultViewModel(
    private val repository: AvgleRepository
) : ViewModel() {

    val keyword = MutableLiveData<String>()
    val keywordClick = MutableLiveData<Event<String>>()

    private lateinit var factory: ResultDataSourceFactory

    val pagedList: LiveData<PagedList<Videos.Video>> = Transformations.switchMap(keyword) {
        factory = ResultDataSourceFactory(viewModelScope, repository, _networkStatus, it)
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    private val _networkStatus = MutableLiveData<Status>()
    val networkStatus: LiveData<Status> = _networkStatus

    fun fetch() {
        keyword.value?.let {
            factory.refresh(it)
        }
    }

    fun saveKeyword(keyword: String) {
        viewModelScope.launch {
            repository.saveKeyword(keyword)
        }
    }

    fun onKeywordClick() {
        keyword.value?.let {
            keywordClick.value = Event(it)
        }
    }
}