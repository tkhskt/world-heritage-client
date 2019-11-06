package com.github.gericass.world_heritage_client.home.category

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    private val _categories = MutableLiveData<Response<List<Categories.Category>>>()
    val categories: LiveData<Response<List<Categories.Category>>> = _categories

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetch() {
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
}