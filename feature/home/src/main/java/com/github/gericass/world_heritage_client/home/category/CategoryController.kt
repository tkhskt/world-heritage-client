package com.github.gericass.world_heritage_client.home.category

import com.airbnb.epoxy.TypedEpoxyController
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.feature.home.homeItemCategory

class CategoryController : TypedEpoxyController<List<Categories.Category>>() {

    override fun buildModels(categories: List<Categories.Category>) {
        categories.forEach {
            homeItemCategory {
                id(it.CHID)
                category(it)
            }
        }
    }
}