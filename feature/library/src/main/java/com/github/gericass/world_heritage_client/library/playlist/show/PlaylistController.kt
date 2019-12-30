package com.github.gericass.world_heritage_client.library.playlist.show

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.view.SmallVideoViewModel_
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.common.vo.SpinnerItem
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.LibraryViewPlaylistHeaderBindingModel_

class PlaylistController(
    private val title: String,
    private val description: String,
    private val videoClickListener: VideoClickListener,
    private val editButtonListener: EditButtonListener,
    private val editable: Boolean,
    private val spinnerItems: List<SpinnerItem>
) : PagedListEpoxyController<Videos.Video>() {

    var isLoading = true
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildItemModel(currentPosition: Int, item: Videos.Video?): EpoxyModel<*> {
        return SmallVideoViewModel_().apply {
            id(currentPosition)
            item?.let {
                video(it)
            }
            draggable(true)
            listener(videoClickListener)
            spinnerItems(spinnerItems)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        LibraryViewPlaylistHeaderBindingModel_().apply {
            id("header")
            title(title)
            description(description)
            editable(editable)
            listener(editButtonListener)
        }.addTo(this)
        super.addModels(models)
        if (isLoading) {
            progressView {
                id("progress")
            }
        }
    }

    interface EditButtonListener {
        fun onEditButtonClick()
    }
}