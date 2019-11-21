package com.github.gericass.world_heritage_client.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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

        val navigationController = navigator.run {
            findNavController(R.id.home_nav_host_fragment)
                .toBottomNavigationController()
        }.apply {
            addOnDestinationChangedListener { _, dest, _ ->
                homeViewModel.currentBottomNavigation = when (dest.label.toString()) {
                    "home" -> R.id.nav_home
                    else -> R.id.nav_library
                }
            }
        }
        homeViewModel.currentBottomNavigation?.let {
            binding.bottomNavigationView.selectedItemId = it
        }
        binding.bottomNavigationView.setupWithNavController(navigationController)
    }

}
