package com.github.gericass.world_heritage_client.home.collection

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Collections
import kotlinx.coroutines.CoroutineScope

class CollectionDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository
) : DataSource.Factory<Int, Collections.Collection>() {

    val collectionDataSource = MutableLiveData<PageKeyedDataSource<Int, Collections.Collection>>()

    override fun create(): DataSource<Int, Collections.Collection> {

        val datasource = CollectionDataSource(scope, repository)

        collectionDataSource.postValue(datasource)

        return datasource
    }
}