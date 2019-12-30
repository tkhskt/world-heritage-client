package com.github.gericass.world_heritage_client.library.playlist.show

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.CoroutineScope

class PlaylistDataSourceFactory(
    private val scope: CoroutineScope,
    private val useCase: PlaylistUseCase,
    private val status: MutableLiveData<Status>
) : DataSource.Factory<Int, Videos.Video>() {

    var playListId: Int? = null
        set(value) {
            field = value
            dataSource.value?.invalidate()
        }

    private val dataSource = MutableLiveData<PlaylistDataSource>()

    override fun create(): DataSource<Int, Videos.Video> {
        val dataSource =
            PlaylistDataSource(
                scope,
                status,
                useCase,
                playListId
            )
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun refresh() {
        dataSource.value?.invalidate()
    }
}