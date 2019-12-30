package com.github.gericass.world_heritage_client.library.playlist.create

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.github.gericass.world_heritage_client.common.dialog.create.NewPlaylistDialog
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryActivityCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatePlaylistActivity : AppCompatActivity() {

    private lateinit var binding: LibraryActivityCreatePlaylistBinding

    private val viewModel: CreatePlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.library_activity_create_playlist)
        setUpToolbar()
        supportFragmentManager.commit {
            replace(R.id.container, CreatePlaylistFragment.newInstance())
        }
        binding.next.setOnClickListener {
            if (viewModel.selectedVideos.size == 0) return@setOnClickListener
            NewPlaylistDialog.show(this) { title ->
                viewModel.createNewPlaylist(title)
                finish()
            }
        }

    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        val icon = ContextCompat.getDrawable(this, R.drawable.common_ic_close_24dp)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
            setHomeAsUpIndicator(icon)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun createIntent(activity: Activity) = Intent(activity, CreatePlaylistActivity::class.java)
    }
}
