package com.github.gericass.world_heritage_client.common

import android.net.Uri
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.data.model.Videos
import com.google.androidbrowserhelper.trusted.TwaLauncher
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment() {

    private val viewModel: BaseViewModel by viewModel()

    abstract val recyclerView: EpoxyRecyclerView

    private val launcher by lazy { TwaLauncher(requireContext()) }

    protected open val videoClickListener = object : VideoClickListener {
        override fun onClick(video: Videos.Video) {
            val builder = TrustedWebActivityIntentBuilder(Uri.parse(video.video_url))
            launcher.launch(builder, null, null)
            viewModel.saveHistory(video)
        }

        override fun onEditClick(video: Videos.Video) {
            viewModel.selectedVideo = video
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.recycledViewPool.clear()
        launcher.destroy()
    }

    protected fun saveVideoToLater() {
        viewModel.saveVideoToLater()
    }

    protected fun showPlaylistDialog() {
        viewModel.showPlaylistDialog(requireActivity().supportFragmentManager)
    }
}