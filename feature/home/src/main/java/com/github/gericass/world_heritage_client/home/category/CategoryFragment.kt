package com.github.gericass.world_heritage_client.home.category


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.BaseFragment
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class CategoryFragment : BaseFragment() {

    private val viewModel: CategoryViewModel by viewModel()

    private lateinit var binding: HomeFragmentCategoryBinding

    private val categoryClickListener = object : CategoryController.CategoryClickListener {
        override fun onClick(category: Categories.Category) {
            viewModel.fetch(category.CHID, category.name)
            categoryController.currentCategory = category.name
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
        binding.recycler.setController(categoryController)
    }

    private fun observeCategories(response: Response<List<Categories.Category>>?) {
        if (response?.status == Status.ERROR) {
            binding.root.showSnackbar(getString(R.string.common_msg_api_error))
            return Timber.e(response.error)
        }
        response?.data?.let {
            categoryController.run {
                categories.addAll(it)
                requestModelBuild()
            }
            viewModel.apply {
                fetch(it.first().CHID, it.first().name)
                categoryController.currentCategory = it.first().name
            }
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        categoryController.submitList(pagedList)
    }

    private fun observeNetworkStatus(status: Status?) {
        when (status ?: return) {
            Status.LOADING -> categoryController.isLoading = true
            Status.SUCCESS -> {
                categoryController.isLoading = false
            }
            Status.ERROR -> run {
                binding.root.showSnackbar(getString(R.string.common_msg_api_error))
                categoryController.isLoading = false
            }
        }
    }

    override fun refresh() {
        // TODO
    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
