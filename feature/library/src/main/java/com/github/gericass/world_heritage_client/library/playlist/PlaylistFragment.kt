package com.github.gericass.world_heritage_client.library.playlist


import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.airbnb.epoxy.EpoxyTouchHelper
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.view.SmallVideoViewModel_
import com.github.gericass.world_heritage_client.common.vo.SpinnerItem
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs


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
            viewModel.editable,
            listOf(
                SpinnerItem(getString(R.string.common_spinner_watch_later), {}),
                SpinnerItem(getString(R.string.common_spinner_playlist), ::showPlaylistDialog)
            )
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
        setUpSwipeToDelete()
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

    private fun setUpSwipeToDelete() {
        EpoxyTouchHelper.initSwiping(recyclerView)
            .left()
            .withTarget(SmallVideoViewModel_::class.java)
            .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<SmallVideoViewModel_>() {

                override fun onSwipeProgressChanged(
                    model: SmallVideoViewModel_?,
                    itemView: View?,
                    swipeProgress: Float,
                    canvas: Canvas?
                ) {
                    val alpha = (abs(swipeProgress) * 255).toInt()
                    if (swipeProgress > 0) {
                        itemView?.setBackgroundColor(Color.argb(alpha, 0, 255, 0))
                    } else {
                        itemView?.setBackgroundColor(Color.argb(alpha, 255, 0, 0))
                    }
                }

                override fun onSwipeCompleted(
                    model: SmallVideoViewModel_?,
                    itemView: View?,
                    position: Int,
                    direction: Int
                ) {
                    val video = model?.video() ?: return
                    viewModel.deleteVideo(video)
                    viewModel.refresh()
                }

                override fun clearView(model: SmallVideoViewModel_?, itemView: View?) {
                    itemView?.setBackgroundColor(Color.WHITE)
                }
            })
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        playlistController.submitList(pagedList)
    }
}
