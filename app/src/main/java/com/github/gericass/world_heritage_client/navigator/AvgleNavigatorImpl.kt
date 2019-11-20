package com.github.gericass.world_heritage_client.navigator

import android.app.Activity
import androidx.navigation.NavController
import com.github.gericass.world_heritage_client.R
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.search.SearchActivity

class AvgleNavigatorImpl : AvgleNavigator {

    override fun NavController.toBottomNavigationController(): NavController {
        val graph = navInflater.inflate(R.navigation.home_nav)
        setGraph(graph)
        return this
    }

    override fun Activity.navigateToSearch(keyword: String) {
        val intent = if (keyword.isEmpty()) {
            SearchActivity.createIntent(this)
        } else {
            SearchActivity.createIntent(this, keyword)
        }
        startActivity(intent)
    }
}