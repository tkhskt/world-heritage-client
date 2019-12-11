package com.github.gericass.world_heritage_client.library.history

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.CommonViewVideoSmallBindingModel_
import com.github.gericass.world_heritage_client.common.period
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import com.github.gericass.world_heritage_client.library.view.dateTextView
import com.github.gericass.world_heritage_client.library.view.historySearchView
import java.util.*

class ViewingHistoryController(
    private val videoClickListener: VideoClickListener,
    private val viewModel: ViewingHistoryViewModel
) : PagedListEpoxyController<ViewingHistory>() {

    private var previousDate = Calendar.getInstance().time

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
            viewModel(viewModel)
            if (viewModel.currentKeyword.isNullOrEmpty()) {
                refresh(true)
            }
        }

        val insertModels = mutableListOf<EpoxyModel<*>>()

        models.forEachIndexed { i, v ->
            val history = v as CommonViewVideoSmallBindingModel_
            val video = history.video()
            val time = video.createdAt ?: return // break
            val period = time.period(previousDate)
            if (i == 0) {
                dateTextView {
                    id("head")
                    date(time)
                }
                previousDate = time
            } else if (i > 0 && period.days != 0) {
                super.addModels(insertModels)
                insertModels.clear()
                dateTextView {
                    id("date-${i}")
                    date(time)
                }
                previousDate = time
            }
            insertModels.add(v)
        }
        if (insertModels.isNotEmpty()) {
            super.addModels(insertModels)
        }
    }
}
