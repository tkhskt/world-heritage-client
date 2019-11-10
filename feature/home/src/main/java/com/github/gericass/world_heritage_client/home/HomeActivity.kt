package com.github.gericass.world_heritage_client.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeActivityHomeBinding
import com.github.gericass.world_heritage_client.home.category.CategoryFragment
import com.github.gericass.world_heritage_client.home.collection.CollectionFragment
import com.github.gericass.world_heritage_client.search.SearchActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityHomeBinding
    private lateinit var pager: ViewPager2
    private lateinit var tab: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_home)
        binding.searchBackground.setOnClickListener {
            val intent = SearchActivity.createIntent(this)
            startActivity(intent)
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
        val pagerAdapter = HomePagerAdapter(supportFragmentManager, lifecycle).apply {
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
