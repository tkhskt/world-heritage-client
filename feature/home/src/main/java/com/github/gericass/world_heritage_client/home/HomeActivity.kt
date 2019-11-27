package com.github.gericass.world_heritage_client.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.feature.home.R
import com.github.gericass.world_heritage_client.feature.home.databinding.HomeActivityHomeBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeActivityHomeBinding

    private val navigator: AvgleNavigator by inject()

    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.home_activity_home)
        setSupportActionBar(binding.toolbarArrow.backArrowToolbar)
        val navigationController = findNavController(R.id.home_nav_host_fragment).apply {
            navigator.run { setHomeGraph() }
            addOnDestinationChangedListener { _, dest, _ ->
                when (dest.label.toString()) {
                    "home" -> run {
                        binding.apply {
                            toolbarDefault.mainToolbar.isVisible = true
                            toolbarArrow.backArrowToolbar.isVisible = false
                            mainTab.isVisible = true
                        }
                    }
                    "history" -> {
                        binding.apply {
                            toolbarArrow.title.text = "履歴"
                            toolbarDefault.mainToolbar.isVisible = false
                            toolbarArrow.backArrowToolbar.isVisible = true
                            mainTab.isVisible = false
                        }
                    }
                    else -> run {
                        binding.apply {
                            toolbarDefault.mainToolbar.isVisible = true
                            toolbarArrow.backArrowToolbar.isVisible = false
                            mainTab.isVisible = false
                        }
                    }
                }
            }
        }
        val appBarConfiguration = navigator.getBottomNavigationConfig()
        setupActionBarWithNavController(navigationController, appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navigationController)
        binding.toolbarDefault.searchBackground.setOnClickListener {
            navigator.run {
                navigateToSearch()
            }
        }
        binding.toolbarArrow.searchButton.setOnClickListener {
            navigator.run {
                navigateToSearch()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.home_nav_host_fragment).navigateUp()

}
