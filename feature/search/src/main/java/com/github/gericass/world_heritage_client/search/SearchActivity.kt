package com.github.gericass.world_heritage_client.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.gericass.world_heritage_client.search.databinding.SearchActivitySearchBinding


class SearchActivity : AppCompatActivity() {

    private lateinit var binding: SearchActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.search_activity_search)
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, SearchActivity::class.java)
    }
}
