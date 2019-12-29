package com.github.gericass.world_heritage_client.search.result

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.view.SmallVideoViewModel_
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.data.model.Videos

class ResultController(
    private val videoClickListener: VideoClickListener
) : PagedListEpoxyController<Videos.Video>() {

    var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    override fun buildItemModel(currentPosition: Int, item: Videos.Video?): EpoxyModel<*> {
        return SmallVideoViewModel_().apply {
            id(currentPosition)
            item?.let {
                video(it)
            }
            listener(videoClickListener)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        super.addModels(models)
        if (isLoading) {
            progressView {
                id(models.size)
            }
        }
    }
}