package com.github.gericass.world_heritage_client.home.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentCategoryBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class CategoryFragment : BaseFragment() {

    private val viewModel: CategoryViewModel by sharedViewModel(
        from = { parentFragment?.parentFragment!! }
    )

    private lateinit var binding: HomeFragmentCategoryBinding

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    private val categoryClickListener = object : CategoryController.CategoryClickListener {
        override fun onClick(category: Categories.Category) {
            viewModel.fetchVideos(category)
            categoryController.run {
                currentCategoryName = category.name
                requestModelBuild()
            }
        }
    }

    private val categoryController: CategoryController =
        CategoryController(categoryClickListener, videoClickListener)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(categories, ::observeCategories)
            observe(pagedList, ::observePagedList)
            observe(networkStatus, ::observeNetworkStatus)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_category, container, false)
        binding = HomeFragmentCategoryBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        categoryController.orientation = requireActivity().resources.configuration.orientation
        binding.apply {
            viewModel = this@CategoryFragment.viewModel
            lifecycleOwner = this@CategoryFragment
            recycler.setController(categoryController)
            refresh.setOnRefreshListener {
                this@CategoryFragment.viewModel.isRefreshing.value = true
                this@CategoryFragment.viewModel.refresh()
            }
        }
    }

    private fun observeCategories(response: Response<List<Categories.Category>>?) {
        if (response?.status == Status.ERROR) {
            showSnackbar(getString(R.string.common_msg_api_error)) {
                viewModel.refresh()
            }
            return Timber.e(response.error)
        }
        val data = response?.data ?: return
        if (viewModel.currentCategory == null) {
            viewModel.fetchVideos(data.first())
        }
        categoryController.run {
            categories.clear()
            categories.addAll(data)
            currentCategoryName = viewModel.currentCategory?.name ?: data.first().name
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        categoryController.submitList(pagedList)
    }

    private fun observeNetworkStatus(status: Status?) {
        when (status ?: return) {
            Status.LOADING -> categoryController.isLoading = run {
                viewModel.isRefreshing.value?.let {
                    if (it) {
                        return@run false
                    }
                }
                return@run true
            }
            Status.SUCCESS -> {
                categoryController.isLoading = false
            }
            Status.ERROR -> {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.refresh()
                }
                categoryController.isLoading = false
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
