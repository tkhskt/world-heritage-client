package com.github.gericass.world_heritage_client.home.collection


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.toast
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Collections
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentCollectionBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class CollectionFragment : Fragment() {

    private val viewModel: CollectionViewModel by viewModel()
    private lateinit var binding: HomeFragmentCollectionBinding
    private lateinit var recyclerView: EpoxyRecyclerView
    private lateinit var collectionController: CollectionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(pagedList, ::observePagedList)
            observe(networkState, ::observeNetworkState)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.home_fragment_collection, container, false)
        binding = HomeFragmentCollectionBinding.bind(view)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.recycler
        setUpList()
    }

    private fun setUpList() {
        val spanCount = 2
        collectionController = CollectionController()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        collectionController.spanCount = spanCount
        layoutManager.spanSizeLookup = collectionController.spanSizeLookup
        recyclerView.setController(collectionController)
        recyclerView.layoutManager = layoutManager
    }


    private fun observePagedList(pagedList: PagedList<Collections.Collection>?) {
        collectionController.submitList(pagedList)
    }

    private fun observeNetworkState(status: Status?) {
        status?.let {
            when (it) {
                Status.LOADING -> collectionController.isLoading = true
                Status.SUCCESS -> collectionController.isLoading = false
                Status.ERROR -> run {
                    toast(getString(R.string.common_msg_api_error))
                    collectionController.isLoading = false
                }

            }
        }
    }

    companion object {
        fun newInstance() = CollectionFragment()
    }

}
