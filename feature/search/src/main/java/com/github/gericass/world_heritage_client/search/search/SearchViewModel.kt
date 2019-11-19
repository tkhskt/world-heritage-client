package com.github.gericass.world_heritage_client.search.search

import androidx.lifecycle.*
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Keyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SearchViewModel(
    private val repository: AvgleRepository
) : ViewModel(), LifecycleObserver {

    val keywordEditText = MutableLiveData<String>()
    private val _keywordLog = MediatorLiveData<List<Keyword>>()
    val keywordLog: LiveData<List<Keyword>> = _keywordLog

    private val _searchButton = MutableLiveData<Unit>()
    val searchButton: LiveData<Unit> = _searchButton

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
            withContext(Dispatchers.IO) {
                val keywords = repository.getAllKeywords()
                _keywordLog.postValue(keywords)
            }
        }
    }

    private fun fetchLog(editTextValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val keyword = if (editTextValue.isEmpty()) {
                    repository.getAllKeywords()
                } else {
                    repository.gerSimilarWords(editTextValue)
                }
                _keywordLog.postValue(keyword)
            }
        }
    }

    fun onSearchClick() {
        _searchButton.value = Unit
    }
}