package com.github.gericass.world_heritage_client.library.history

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.CommonViewVideoSmallBindingModel_
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import com.github.gericass.world_heritage_client.library.view.historySearchView

class ViewingHistoryController(
    private val videoClickListener: VideoClickListener
) : PagedListEpoxyController<ViewingHistory>() {

    override fun buildItemModel(currentPosition: Int, item: ViewingHistory?): EpoxyModel<*> {
        return CommonViewVideoSmallBindingModel_().apply {
            id(currentPosition)
            item?.let {
                video(it.toVideo())
            }
            listener(videoClickListener)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        historySearchView {
            id("search")
        }
        super.addModels(models)
    }
}
