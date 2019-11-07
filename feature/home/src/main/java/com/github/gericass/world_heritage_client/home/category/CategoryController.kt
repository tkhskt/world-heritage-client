package com.github.gericass.world_heritage_client.home.category

import com.airbnb.epoxy.Typed2EpoxyController
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.feature.home.homeItemCategory

class CategoryController : Typed2EpoxyController<List<Categories.Category>?, Boolean>() {
    
    override fun buildModels(categories: List<Categories.Category>?, loading: Boolean) {
        categories?.forEach {
            homeItemCategory {
                id(it.CHID)
                category(it)
            }
        }
        if (loading) {
            progressView {
                id(categories?.size)
            }
        }
    }
}