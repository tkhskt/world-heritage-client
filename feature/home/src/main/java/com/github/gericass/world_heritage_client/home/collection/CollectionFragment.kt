package com.github.gericass.world_heritage_client.home.collection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentCollectionBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class CollectionFragment : BaseFragment() {

    private val viewModel: CollectionViewModel by sharedViewModel(
        from = {
            parentFragment?.parentFragment!!
        }
    )

    private val navigator: AvgleNavigator by inject()

    private lateinit var binding: HomeFragmentCollectionBinding

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    private val collectionClickListener = object : CollectionController.CollectionClickListener {
        override fun onClick(keyword: String) {
            navigator.run {
                requireActivity().navigateToSearch(keyword)
            }
        }
    }

    private val collectionController = CollectionController(collectionClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(pagedList, ::observePagedList)
            observe(networkStatus, ::observeNetworkStatus)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_collection, container, false)
        binding = HomeFragmentCollectionBinding.bind(view)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.refresh.apply {
            setOnRefreshListener {
                viewModel.isRefreshing.value = true
                viewModel.refresh()
            }
        }
    }

    private fun setUpList() {
        val spanCount = 2
        val manager = GridLayoutManager(requireContext(), spanCount)
        collectionController.spanCount = spanCount
        manager.spanSizeLookup = collectionController.spanSizeLookup
        binding.recycler.apply {
            setController(collectionController)
            layoutManager = manager
        }
    }


    private fun observePagedList(pagedList: PagedList<Collections.Collection>?) {
        collectionController.submitList(pagedList)
    }

    private fun observeNetworkStatus(status: Status?) {
        when (status) {
            Status.LOADING -> collectionController.isLoading = run {
                viewModel.isRefreshing.value?.let { refreshing ->
                    if (refreshing) return@run false
                }
                return@run true
            }
            Status.SUCCESS -> collectionController.isLoading = false
            Status.ERROR -> run {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.refresh()
                }
                collectionController.isLoading = false
            }
        }

    }

    companion object {
        fun newInstance() = CollectionFragment()
    }

}
