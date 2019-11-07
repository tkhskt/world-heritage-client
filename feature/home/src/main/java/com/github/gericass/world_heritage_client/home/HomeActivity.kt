package com.github.gericass.world_heritage_client.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeActivityHomeBinding
import com.github.gericass.world_heritage_client.home.category.CategoryFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityHomeBinding
    private lateinit var pager: ViewPager2
    private lateinit var tab: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_home)
        binding.appBar
        setUpViewPager()
        setUpTab()
    }

    private fun setUpTab() {
        tab = binding.mainTab
        // AutoRefreshは後からどっちか調整した方が良さげ
        TabLayoutMediator(tab, pager, true) { tab, position ->
            tab.text = "Category"
            //tab.text = when (position) {
            //    //0 -> getString(R.string.activity)
            //    //1 -> getString(R.string.overview)
            //    //else -> getString(R.string.repositories)
            //}
        }.attach()
    }

    private fun setUpViewPager() {
        val pagerAdapter = HomePagerAdapter(supportFragmentManager, lifecycle).apply {
            addFragment(CategoryFragment.newInstance())
        }
        pager = binding.mainPager
        pager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = pagerAdapter
            isUserInputEnabled = false
        }
    }
}
