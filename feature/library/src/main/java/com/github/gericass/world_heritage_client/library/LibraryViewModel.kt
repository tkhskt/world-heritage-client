package com.github.gericass.world_heritage_client.library

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import kotlinx.coroutines.launch

class LibraryViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    private val _history = MutableLiveData<List<ViewingHistory>>()
    val history: LiveData<List<ViewingHistory>> = _history

    val isRefreshing = MutableLiveData<Boolean>()


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun fetchHistories() {
        viewModelScope.launch {
            _history.value = repository.getViewingHistories(15)
        }
    }
}