package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections
import kotlinx.coroutines.CoroutineScope

class CollectionDataSourceFactory(
    scope: CoroutineScope,
    repository: AvgleRepository
) : DataSource.Factory<Int, Collections.Collection>() {

    private val dataSource = CollectionDataSource(scope, repository)

    override fun create(): DataSource<Int, Collections.Collection> {
        return dataSource
    }

    fun getNetworkState(): LiveData<Status> = dataSource.networkState
}