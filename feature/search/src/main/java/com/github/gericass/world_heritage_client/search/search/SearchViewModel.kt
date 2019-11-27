package com.github.gericass.world_heritage_client.search.search

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Keyword
import kotlinx.coroutines.launch


class SearchViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    val keywordEditText = MutableLiveData<String>()
    private val _keywordLog = MediatorLiveData<List<Keyword>>()
    val keywordLog: LiveData<List<Keyword>> = _keywordLog

    private val _searchButton = MutableLiveData<Event<String>>()
    val searchButton: LiveData<Event<String>> = _searchButton

    init {
        val editTextObserver = Observer<String> {
            val editText = keywordEditText.value ?: return@Observer
            fetchLog(editText)
        }
        _keywordLog.addSource(keywordEditText, editTextObserver)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun init() {
        viewModelScope.launch {
            if (keywordEditText.value.isNullOrEmpty()) {
                val keywords = repository.getAllKeywords()
                _keywordLog.postValue(keywords)
            }
        }
    }

    private fun fetchLog(editTextValue: String) {
        viewModelScope.launch {
            val keyword = if (editTextValue.isEmpty()) {
                repository.getAllKeywords()
            } else {
                repository.gerSimilarWords(editTextValue)
            }
            _keywordLog.postValue(keyword)
        }
    }

    fun onSearchClick() {
        keywordEditText.value?.let {
            _searchButton.value = Event(it)
        }
    }
}