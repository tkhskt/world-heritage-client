package com.github.gericass.world_heritage_client.common.navigator

import android.app.Activity
import androidx.navigation.NavController

interface AvgleNavigator {

    fun NavController.toBottomNavigationController(): NavController

    fun Activity.navigateToSearch(keyword: String = "")
}