package com.github.gericass.world_heritage_client.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeFragmentHomeBinding
import com.github.gericass.world_heritage_client.home.category.CategoryFragment
import com.github.gericass.world_heritage_client.home.collection.CollectionFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.ext.android.inject


class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentHomeBinding
    private lateinit var pager: ViewPager2
    private lateinit var tab: TabLayout

    private val navigator: AvgleNavigator by inject()

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
        binding.searchBackground.setOnClickListener {
            navigator.run {
                requireActivity().navigateToSearch()
            }
        }
        setUpViewPager()
        setUpTab()
    }

    private fun setUpTab() {
        tab = binding.mainTab
        // AutoRefreshは後からどっちか調整した方が良さげ
        TabLayoutMediator(tab, pager, true) { tab, position ->
            tab.text = when (position) {
                0 -> "Category"
                //1 -> getString(R.string.overview)
                else -> "Collection"
            }
        }.attach()
    }

    private fun setUpViewPager() {
        val pagerAdapter = HomePagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle
        ).apply {
            addFragment(CategoryFragment.newInstance())
            addFragment(CollectionFragment.newInstance())
        }
        pager = binding.mainPager
        pager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = pagerAdapter
            isUserInputEnabled = false
        }
    }

}

