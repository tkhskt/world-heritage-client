package com.github.gericass.world_heritage_client.home.category

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    private val _categories = MutableLiveData<Response<List<Categories.Category>>>()
    val categories: LiveData<Response<List<Categories.Category>>> = _categories

    val pagedList: LiveData<PagedList<Videos.Video>>

    private val _networkStatus = MutableLiveData<Status>()
    val networkStatus: LiveData<Status> = _networkStatus

    private val factory = CategoryDataSourceFactory(viewModelScope, repository, _networkStatus)


    var currentCategoryName: String = ""

    init {
        val pagedListConfig = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(50).build()
        pagedList = LivePagedListBuilder(factory, pagedListConfig)
            .build()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        viewModelScope.launch {
            try {
                val categories = withContext(Dispatchers.IO) {
                    repository.getCategories()
                }
                _categories.value = Response.onSuccess(categories.response.categories)
            } catch (e: Throwable) {
                _categories.value = Response.onError(e)
            }
        }
    }

    fun fetch(categoryId: String, categoryName: String) {
        factory.setNewCategoryId(categoryId)
        currentCategoryName = categoryName
    }
}