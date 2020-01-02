package com.github.gericass.world_heritage_client.library.playlist.create

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.view.SmallVideoViewModel_
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.view.SelectedCountViewModel_

class CreatePlaylistController(
    private val videoClickListener: VideoClickListener,
    private val viewModel: CreatePlaylistViewModel
) : PagedListEpoxyController<Videos.Video>() {

    var isLoading = true
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildItemModel(currentPosition: Int, item: Videos.Video?): EpoxyModel<*> {
        val selectedVideoId = viewModel.selectedVideos.map { it.vid }
        return SmallVideoViewModel_().apply {
            id(currentPosition)
            item?.let {
                video(it)
                if (selectedVideoId.contains(it.vid)) {
                    isChecked(true)
                }
            }
            listener(videoClickListener)
            checkable(true)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        SelectedCountViewModel_().apply {
            id("count")
            viewModel(viewModel)
        }.addTo(this)
        super.addModels(models)
        if (isLoading) {
            progressView {
                id("progress")
            }
        }
    }
}