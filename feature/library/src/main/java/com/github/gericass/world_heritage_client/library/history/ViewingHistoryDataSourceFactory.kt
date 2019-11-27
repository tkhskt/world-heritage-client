package com.github.gericass.world_heritage_client.library.history

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import kotlinx.coroutines.CoroutineScope

class ViewingHistoryDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val status: MutableLiveData<Status>
) : DataSource.Factory<Int, ViewingHistory>() {

    private var keyword: String? = null
    private val dataSource = MutableLiveData<ViewingHistoryDataSource>()

    override fun create(): DataSource<Int, ViewingHistory> {
        val dataSource = ViewingHistoryDataSource(scope, repository, status, keyword)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun refresh() {
        dataSource.value?.invalidate()
    }

    fun setNewKeyword(keyword: String?) {
        this.keyword = keyword
        dataSource.value?.invalidate()
    }
}