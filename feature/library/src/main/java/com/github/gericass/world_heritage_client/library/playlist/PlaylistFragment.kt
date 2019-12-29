package com.github.gericass.world_heritage_client.library.playlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : BaseFragment() {

    private lateinit var binding: LibraryFragmentPlaylistBinding

    private val args: PlaylistFragmentArgs by navArgs()

    private val viewModel: PlaylistViewModel by viewModel()

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    private val editButtonListener = object : PlaylistController.EditButtonListener {
        override fun onEditButtonClick() {

        }
    }

    private val playlistController by lazy {
        PlaylistController(
            viewModel.title,
            viewModel.description,
            videoClickListener,
            editButtonListener,
            viewModel.editable
        )
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
        val view = inflater.inflate(R.layout.library_fragment_playlist, container, false)
        binding = LibraryFragmentPlaylistBinding.bind(view)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        viewModel.apply {
            editable = args.editable
            playlistId = args.playlistId
            title = args.playlistTitle
            description = args.playlistDescription
        }
        binding.apply {
            lifecycleOwner = this@PlaylistFragment
            viewModel = this@PlaylistFragment.viewModel
            recycler.setController(playlistController)
            refresh.setOnRefreshListener {
                this@PlaylistFragment.viewModel.isRefreshing.value = true
                this@PlaylistFragment.viewModel.refresh()
            }
        }
    }

    private fun observeLoadingStatus(status: Status?) {
        when (status) {
            Status.LOADING -> playlistController.isLoading = run {
                viewModel.isRefreshing.value?.let { refreshing ->
                    if (refreshing) return@run false
                }
                return@run true
            }
            Status.SUCCESS -> playlistController.isLoading = false
            Status.ERROR -> run {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.refresh()
                }
                playlistController.isLoading = false
            }
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        playlistController.submitList(pagedList)
    }
}
