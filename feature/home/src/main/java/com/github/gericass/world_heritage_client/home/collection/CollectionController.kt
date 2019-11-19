package com.github.gericass.world_heritage_client.home.collection

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.feature.home.HomeItemCollectionBindingModel_

class CollectionController(
    private val collectionClickListener: CollectionClickListener
) : PagedListEpoxyController<Collections.Collection>() {

    var isLoading: Boolean = false
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    override fun buildItemModel(
        currentPosition: Int,
        item: Collections.Collection?
    ): EpoxyModel<*> {
        return HomeItemCollectionBindingModel_().apply {
            id(currentPosition)
            item?.let {
                collection(it)
            }
            listener(collectionClickListener)
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

    interface CollectionClickListener {
        fun onClick(keyword: String)
    }
}