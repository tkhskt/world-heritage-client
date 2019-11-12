package com.github.gericass.world_heritage_client.search.result

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.CoroutineScope


class ResultDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val status: MutableLiveData<Status>,
    private var keyword: String? = null
) : DataSource.Factory<Int, Videos.Video>() {

    private val dataSource = MutableLiveData<ResultDataSource>()

    override fun create(): DataSource<Int, Videos.Video> {
        val dataSource = ResultDataSource(scope, repository, status, keyword)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun refresh(keyword: String) {
        this.keyword = keyword
        dataSource.value?.invalidate()
    }
}