package com.github.gericass.world_heritage_client.home.category

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.carousel
import com.airbnb.epoxy.paging.PagedListEpoxyController
import com.github.gericass.world_heritage_client.common.CommonViewVideoBindingModel_
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.view.progressView
import com.github.gericass.world_heritage_client.common.view.subjectTextView
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.feature.home.HomeItemCategoryBindingModel_

class CategoryController(
    private val categoryClickListener: CategoryClickListener,
    private val videoClickListener: VideoClickListener
) : PagedListEpoxyController<Videos.Video>() {

    val categories = mutableListOf<Categories.Category>()

    var currentCategory: String = ""
        set(value) {
            field = value
            requestModelBuild()
        }

    var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    override fun buildItemModel(currentPosition: Int, item: Videos.Video?): EpoxyModel<*> {
        return CommonViewVideoBindingModel_().apply {
            id(currentPosition)
            item?.let {
                video(it)
            }
            listener(videoClickListener)
        }
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        val categoryModels = categories.map {
            HomeItemCategoryBindingModel_().apply {
                id(it.CHID)
                category(it)
                listener(categoryClickListener)
            }
        }
        subjectTextView {
            id("subject_category")
            text("カテゴリー")
            withPaddingStyle()
        }
        carousel {
            id("category")
            numViewsToShowOnScreen(3f)
            models(categoryModels)
        }
        subjectTextView {
            id("subject_description")
            text(currentCategory)
            withPaddingStyle()
        }
        super.addModels(models)
        if (isLoading) {
            progressView {
                id(models.size)
            }
        }
    }

    interface CategoryClickListener {
        fun onClick(category: Categories.Category)
    }
}