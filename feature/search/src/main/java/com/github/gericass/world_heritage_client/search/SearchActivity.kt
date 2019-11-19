package com.github.gericass.world_heritage_client.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.github.gericass.world_heritage_client.search.databinding.SearchActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_activity_search)
        setUpNavigation()
    }


    private fun setUpNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val navGraph = navController.navInflater.inflate(R.navigation.search_nav)
        intent.getStringExtra(KEYWORD)?.let {
            navGraph.startDestination = R.id.result_fragment
        } ?: run {
            navGraph.startDestination = R.id.search_fragment
        }
        navController.graph = navGraph
    }

    companion object {
        private const val KEYWORD = "keyword"
        fun createIntent(activity: Activity, keyword: String? = null) =
            Intent(activity, SearchActivity::class.java).apply {
                putExtra(KEYWORD, keyword)
            }
    }
}
