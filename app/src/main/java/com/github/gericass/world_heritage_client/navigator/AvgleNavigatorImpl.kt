package com.github.gericass.world_heritage_client.navigator

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.github.gericass.world_heritage_client.R
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.search.SearchActivity

class AvgleNavigatorImpl : AvgleNavigator {

    override fun NavController.setHomeGraph() {
        setGraph(R.navigation.home_nav)
    }

    override fun getBottomNavigationConfig(): AppBarConfiguration {
        return AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_library)
        )
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