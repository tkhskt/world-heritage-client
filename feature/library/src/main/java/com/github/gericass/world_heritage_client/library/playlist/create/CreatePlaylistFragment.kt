package com.github.gericass.world_heritage_client.library.playlist.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class CreatePlaylistFragment : BaseFragment() {

    private lateinit var binding: LibraryFragmentCreatePlaylistBinding

    private val viewModel: CreatePlaylistViewModel by sharedViewModel()

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    override val videoClickListener = object : VideoClickListener {
        override fun onClick(video: Videos.Video) {
            if (!viewModel.selectedVideos.remove(video)) {
                viewModel.selectedVideos.add(video)
            }
            viewModel.selectedItemCount.value = viewModel.selectedVideos.size
            Timber.d(viewModel.selectedItemCount.value.toString())
        }

        override fun onEditClick(video: Videos.Video) {
            // do nothing
        }
    }

    private val controller by lazy {
        CreatePlaylistController(videoClickListener, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(loadingStatus, ::observeLoadingStatus)
            observe(pagedList, ::observePagedList)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_create_playlist, container, false)
        binding = LibraryFragmentCreatePlaylistBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel = this@CreatePlaylistFragment.viewModel
            lifecycleOwner = this@CreatePlaylistFragment
            recycler.setController(controller)
            refresh.setOnRefreshListener {
                this@CreatePlaylistFragment.viewModel.isRefreshing.value = true
                this@CreatePlaylistFragment.viewModel.refresh()
            }
        }
    }


    private fun observeLoadingStatus(status: Status?) {
        when (status) {
            Status.LOADING -> controller.isLoading = run {
                viewModel.isRefreshing.value?.let { refreshing ->
                    if (refreshing) return@run false
                }
                return@run true
            }
            Status.SUCCESS -> controller.isLoading = false
            Status.ERROR -> run {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.refresh()
                }
                controller.isLoading = false
            }
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        controller.submitList(pagedList)
    }

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }
}
