package com.github.gericass.world_heritage_client.common.navigator

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration

interface AvgleNavigator {

    fun NavController.setHomeGraph()

    fun getBottomNavigationConfig(): AppBarConfiguration

    fun Activity.navigateToSearch(keyword: String = "")

    interface LibraryNavigator {
        fun NavController.navigateToHistory()
        fun NavController.navigateToFavorite(playlistId: Int, editable: Boolean)
    }
}