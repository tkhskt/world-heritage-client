package com.github.gericass.world_heritage_client.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentHomeBinding
import com.github.gericass.world_heritage_client.home.category.CategoryFragment
import com.github.gericass.world_heritage_client.home.category.CategoryViewModel
import com.github.gericass.world_heritage_client.home.collection.CollectionFragment
import com.github.gericass.world_heritage_client.home.collection.CollectionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.home_activity_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentHomeBinding
    private lateinit var pager: ViewPager2
    private lateinit var tab: TabLayout

    private val homeViewModel: HomeViewModel by sharedViewModel()

    private val categoryViewModel: CategoryViewModel by viewModel()
    private val collectionViewModel: CollectionViewModel by viewModel()

    private val pagerCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            homeViewModel.currentPage = position
        }
    }

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_fragment_home, container, false)
        binding = HomeFragmentHomeBinding.bind(view)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()
        setUpTab()
    }

    private fun setUpTab() {
        // ActivityのViewを参照しているのでonDestroyのタイミングで参照を切る必要がある
        tab = requireActivity().main_tab
        tabLayoutMediator = TabLayoutMediator(tab, pager, true) { tab, position ->
            tab.text = when (position) {
                0 -> "Category"
                else -> "Collection"
            }
        }.apply {
            attach()
        }
    }

    private fun setUpViewPager() {
        val pagerAdapter = HomePagerAdapter(
            childFragmentManager,
            lifecycle
        ).apply {
            addFragment(CategoryFragment.newInstance())
            addFragment(CollectionFragment.newInstance())
        }
        pager = binding.mainPager
        pager.apply {
            currentItem = homeViewModel.currentPage
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = pagerAdapter
            isUserInputEnabled = false
            registerOnPageChangeCallback(pagerCallback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pager.unregisterOnPageChangeCallback(pagerCallback)
        tabLayoutMediator.detach()
    }
}

