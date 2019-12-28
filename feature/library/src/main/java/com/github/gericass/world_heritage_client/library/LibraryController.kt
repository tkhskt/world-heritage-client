package com.github.gericass.world_heritage_client.library

import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.github.gericass.world_heritage_client.common.commonViewBorder
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.view.subjectTextView
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import com.github.gericass.world_heritage_client.library.view.playlistView


class LibraryController(
    private val videoClickListener: VideoClickListener,
    private val playlistClickListener: (Playlist) -> Unit
) : EpoxyController() {

    var history: List<ViewingHistory>? = null
        set(value) {
            field = value
            requestModelBuild()
        }
    var playlists: List<Playlist>? = null
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        subjectTextView {
            id("recent_contents")
            text("最近視聴したコンテンツ")
            withPaddingStyle()
            sectionStyle(true)
        }
        val videos = history?.map {
            LibraryViewVideoBindingModel_().apply {
                id(it.vid)
                video(it.toVideo())
                listener(videoClickListener)
            }
        } ?: return
        Carousel.setDefaultGlobalSnapHelperFactory(null)
        carousel {
            id("history")
            models(videos)
        }
        commonViewBorder {
            id("carousel_border")
        }
        playlists?.forEach {
            playlistView {
                id(it.id)
                playlist(it)
                clickListener(playlistClickListener)
            }
        }
    }

}