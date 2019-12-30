package com.github.gericass.world_heritage_client.library.playlist.create

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.CoroutineScope

class CreatePlaylistDataSouceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val status: MutableLiveData<Status>
) : DataSource.Factory<Int, Videos.Video>() {

    private val dataSource = MutableLiveData<CreatePlaylistDataSource>()

    override fun create(): DataSource<Int, Videos.Video> {
        val dataSource = CreatePlaylistDataSource(scope, repository, status)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun refresh() {
        dataSource.value?.invalidate()
    }
}