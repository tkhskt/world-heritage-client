package com.github.gericass.world_heritage_client.home.category

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    private val _categories = MutableLiveData<Response<List<Categories.Category>>>()
    val categories: LiveData<Response<List<Categories.Category>>> = _categories

    private val _networkStatus = MutableLiveData<Status>()
    val networkStatus: LiveData<Status> = _networkStatus

    val isRefreshing = MediatorLiveData<Boolean>()

    private val factory = CategoryDataSourceFactory(viewModelScope, repository, _networkStatus)

    val pagedList: LiveData<PagedList<Videos.Video>>

    var currentCategory: Categories.Category? = null

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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        if (currentCategory != null) return
        fetchCategories()
    }

    fun fetchVideos(category: Categories.Category) {
        // val refreshing = isRefreshing.value ?: false
        if (category.CHID != currentCategory?.CHID) {
            currentCategory = category
            factory.setNewCategory(category.CHID)
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val categories = repository.getCategories()
                _categories.value = Response.onSuccess(categories.response.categories)
            } catch (e: Throwable) {
                _categories.value = Response.onError(e)
            }
        }
    }

    fun refresh() {
        currentCategory = null
        fetchCategories()
    }
}