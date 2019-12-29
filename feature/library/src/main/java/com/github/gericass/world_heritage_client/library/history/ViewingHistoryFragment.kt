package com.github.gericass.world_heritage_client.library.history


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.SpinnerItem
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.ViewingHistory
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentViewingHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewingHistoryFragment : BaseFragment() {

    private lateinit var binding: LibraryFragmentViewingHistoryBinding

    private val viewModel: ViewingHistoryViewModel by viewModel()

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    private val historyController by lazy {
        ViewingHistoryController(
            videoClickListener,
            viewModel,
            listOf(
                SpinnerItem("[後で見る]に追加", {}),
                SpinnerItem("再生リストに追加", ::showPlaylistDialog)
            )
        )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(pagedList, ::observePagedList)
            observe(loadingStatus, ::observeLoadingStatus)
            observe(searchButton, ::observeSearchButton)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_viewing_history, container, false)
        binding = LibraryFragmentViewingHistoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        binding.apply {
            viewModel = this@ViewingHistoryFragment.viewModel
            lifecycleOwner = this@ViewingHistoryFragment
            recycler.setController(historyController)
            refresh.setOnRefreshListener {
                this@ViewingHistoryFragment.viewModel.isRefreshing.value = true
                this@ViewingHistoryFragment.viewModel.refresh()
            }
        }
    }

    private fun observeLoadingStatus(status: Status?) {
        if (status == Status.ERROR) {
            showSnackbar(getString(R.string.common_msg_local_error)) {
                viewModel.refresh()
            }
        }
    }

    private fun observeSearchButton(event: Event<String>?) {
        event?.getContentIfNotHandled()?.let {
            if (it.isEmpty()) return viewModel.refresh()
            viewModel.fetch(it)
        }
    }

    private fun observePagedList(pagedList: PagedList<ViewingHistory>?) {
        historyController.submitList(pagedList)
    }

}
