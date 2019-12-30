package com.github.gericass.world_heritage_client.library.playlist.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.gericass.world_heritage_client.library.R


class CreatePlaylistFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.library_fragment_create_playlist, container, false)
    }

    companion object {

        fun newInstance() =
            CreatePlaylistFragment()
    }
}
