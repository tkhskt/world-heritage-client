package com.github.gericass.world_heritage_client.home.category

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.AvgleRepository
import com.github.gericass.world_heritage_client.data.model.Videos
import kotlinx.coroutines.CoroutineScope

class CategoryDataSourceFactory(
    private val scope: CoroutineScope,
    private val repository: AvgleRepository,
    private val status: MutableLiveData<Status>,
    private var categoryId: String? = null
) : DataSource.Factory<Int, Videos.Video>() {

    private val dataSource = MutableLiveData<CategoryDataSource>()

    override fun create(): DataSource<Int, Videos.Video> {
        val dataSource = CategoryDataSource(scope, repository, status, categoryId)
        this.dataSource.postValue(dataSource)
        return dataSource
    }

    fun setNewCategoryId(categoryId: String) {
        this.categoryId = categoryId
        dataSource.value?.run {
            invalidate()
        }
    }
}