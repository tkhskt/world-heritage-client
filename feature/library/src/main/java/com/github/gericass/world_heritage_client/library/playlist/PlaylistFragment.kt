package com.github.gericass.world_heritage_client.library.playlist


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentPlaylistBinding

class PlaylistFragment : BaseFragment() {

    private lateinit var binding: LibraryFragmentPlaylistBinding

    private val args: PlaylistFragmentArgs by navArgs()

    override val recyclerView: EpoxyRecyclerView by lazy { binding.recycler }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_playlist, container, false)
        binding = LibraryFragmentPlaylistBinding.bind(view)
        return binding.root
    }


}
