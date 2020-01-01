package com.github.gericass.world_heritage_client.navigator

import android.app.Activity
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import com.github.gericass.world_heritage_client.R
import com.github.gericass.world_heritage_client.common.navigator.AvgleNavigator
import com.github.gericass.world_heritage_client.home.HomeActivity
import com.github.gericass.world_heritage_client.library.LibraryFragmentDirections
import com.github.gericass.world_heritage_client.library.playlist.create.CreatePlaylistActivity
import com.github.gericass.world_heritage_client.search.SearchActivity

class AvgleNavigatorImpl :
    AvgleNavigator,
    AvgleNavigator.LibraryNavigator,
    AvgleNavigator.LoginNavigator {

    override fun NavController.setHomeGraph() {
        setGraph(R.navigation.common_home_navigation)
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

    override fun NavController.navigateToHistory() {
        navigate(R.id.action_library_to_history)
    }

    override fun NavController.navigateToFavorite(
        playlistId: Int,
        playlistTitle: String,
        playlistDescription: String
    ) {
        navigate(
            LibraryFragmentDirections.actionLibraryToPlaylist(
                playlistId,
                playlistTitle,
                playlistDescription,
                false
            )
        )
    }

    override fun NavController.navigateToLater(
        playlistId: Int,
        playlistTitle: String,
        playlistDescription: String
    ) {
        navigate(
            LibraryFragmentDirections.actionLibraryToPlaylist(
                playlistId,
                playlistTitle,
                playlistDescription,
                false
            )
        )
    }

    override fun NavController.navigateToPlaylist(
        playlistId: Int,
        playlistTitle: String,
        playlistDescription: String
    ) {
        navigate(
            LibraryFragmentDirections.actionLibraryToPlaylist(
                playlistId,
                playlistTitle,
                playlistDescription,
                true
            )
        )
    }

    override fun Activity.navigateToNewPlaylist() {
        val intent = CreatePlaylistActivity.createIntent(this)
        startActivity(intent)
    }

    override fun Activity.navigateToHome() {
        val intent = HomeActivity.createIntent(this)
        startActivity(intent)
    }
}