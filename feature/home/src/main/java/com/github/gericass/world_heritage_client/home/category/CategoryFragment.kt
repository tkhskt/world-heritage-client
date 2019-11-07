package com.github.gericass.world_heritage_client.home.category


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.toast
import com.github.gericass.world_heritage_client.common.vo.Response
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Categories
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentCategoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class CategoryFragment : Fragment() {

    private val viewModel: CategoryViewModel by viewModel()

    private lateinit var binding: HomeFragmentCategoryBinding
    private lateinit var recyclerView: EpoxyRecyclerView
    private lateinit var categoryController: CategoryController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(categories, ::observeCategories)
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
        recyclerView = binding.recycler
        setUpList()
    }

    private fun setUpList() {
        categoryController = CategoryController()
        recyclerView.setController(categoryController)
        //recyclerView.apply {
        //    val manager = LinearLayoutManager(requireContext())
        //    layoutManager = manager
        //    adapter = categoryController.adapter
        //}

    }

    private fun observeCategories(response: Response<List<Categories.Category>>?) {
        binding.progress.isVisible = false
        if (response?.status == Status.ERROR) {
            toast(getString(R.string.common_msg_api_error))
            return Timber.e(response.error)
        }
        response?.data?.let {
            categoryController.run {
                setData(it)
                //requestModelBuild()
            }
        }

    }

    companion object {

        @JvmStatic
        fun newInstance() = CategoryFragment()
    }
}
