package com.github.gericass.world_heritage_client.home.category


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentCategoryBinding
import com.google.androidbrowserhelper.trusted.TwaLauncher
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by sharedViewModel(
        from = { parentFragment?.parentFragment!! }
    )

    private lateinit var binding: HomeFragmentCategoryBinding

    private val categoryClickListener = object : CategoryController.CategoryClickListener {
        override fun onClick(category: Categories.Category) {
            viewModel.fetchVideos(category)
            categoryController.run {
                currentCategoryName = category.name
                requestModelBuild()
            }
        }
    }

    private val videoClickListener = object : VideoClickListener {
        override fun onClick(video: Videos.Video) {
            val builder = TrustedWebActivityIntentBuilder(Uri.parse(video.video_url))
            TwaLauncher(requireContext()).launch(builder, null, null)
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
        viewModel.apply {
            if (viewModel.currentCategory == null) {
                fetchVideos(data.first())
            }
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
            Status.ERROR -> run {
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
