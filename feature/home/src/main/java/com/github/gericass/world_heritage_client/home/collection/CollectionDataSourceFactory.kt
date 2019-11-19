package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections
import kotlinx.coroutines.CoroutineScope

class CollectionDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val status: MutableLiveData<Status>
) : DataSource.Factory<Int, Collections.Collection>() {

    private val dataSource = MutableLiveData<CollectionDataSource>()

    override fun create(): DataSource<Int, Collections.Collection> {
        val ds = CollectionDataSource(scope, repository, status)
        dataSource.postValue(ds)
        return ds
    }

    fun refresh() {
        dataSource.value?.run {
            invalidate()
        }
    }
}