package com.github.gericass.world_heritage_client.library


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.data.model.PlayList
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentLibraryBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LibraryFragment : BaseFragment() {

    private val viewModel: LibraryViewModel by viewModel()

    private lateinit var binding: LibraryFragmentLibraryBinding

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    private val navigator: AvgleNavigator.LibraryNavigator by inject()

    private val playListClickListener = { playList: PlayList ->
        if (playList.id == 0L) {
            navigator.run {
                findNavController().navigateToHistory()
            }
        }
    }

    private val libraryController = LibraryController(videoClickListener, playListClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(history, ::observeHistories)
            observe(playLists, ::observePlayLists)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_library, container, false)
        binding = LibraryFragmentLibraryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        binding.apply {
            viewModel = this@LibraryFragment.viewModel
            lifecycleOwner = this@LibraryFragment
            recycler.setController(libraryController)
            refresh.setOnRefreshListener {
                this@LibraryFragment.viewModel.isRefreshing.value = true
                this@LibraryFragment.viewModel.refresh()
            }

        }
    }

    private fun observeHistories(histories: List<ViewingHistory>?) {
        libraryController.history = histories
    }

    private fun observePlayLists(playLists: List<PlayList>?) {
        val history = PlayList(0, "履歴", R.drawable.common_ic_history_24dp)
        val list = playLists?.toMutableList()?.apply {
            add(0, history)
        }
        libraryController.playLists = list
        viewModel.isRefreshing.value = false
    }
}
