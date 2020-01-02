package com.github.gericass.world_heritage_client.library.playlist.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import com.github.gericass.world_heritage_client.common.hideKeyboard
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryActivityEditPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistActivity : AppCompatActivity() {

    private lateinit var binding: LibraryActivityEditPlaylistBinding

    private val viewModel: EditPlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.library_activity_edit_playlist)
        viewModel.playlistId = intent.getIntExtra(ID, 0)
        setUpToolbar()
        supportFragmentManager.commit {
            replace(
                R.id.container,
                EditPlaylistFragment.newInstance()
            )
        }
    }

    private fun setUpToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }
        binding.button.setOnClickListener {
            hideKeyboard(binding.root)
            if (viewModel.titleEditText.value.isNullOrEmpty()) return@setOnClickListener
            viewModel.updatePlaylistInfo()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val ID = "id"
        fun createIntent(activity: Activity, playlistId: Int) =
            Intent(activity, EditPlaylistActivity::class.java).apply {
                putExtra(ID, playlistId)
            }
    }
}
