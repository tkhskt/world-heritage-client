package com.github.gericass.world_heritage_client.library.playlist.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentEditPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class EditPlaylistFragment : Fragment() {

    private lateinit var binding: LibraryFragmentEditPlaylistBinding

    private val viewModel: EditPlaylistViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(update, ::observeUpdate)
            observe(playlist, ::observePlaylist)
            observe(status, ::observeStatus)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_edit_playlist, container, false)
        binding = LibraryFragmentEditPlaylistBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.addObserver(viewModel)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun observePlaylist(resp: Playlist?) {
        val playlist = resp ?: return
        viewModel.titleEditText.value = playlist.title
        viewModel.descriptionEditText.value = playlist.description
        viewModel.thumbnail.value = playlist.thumbnailImgUrl ?: return
    }

    private fun observeUpdate(event: Event<Status>?) {
        val status = event?.getContentIfNotHandled() ?: return
        if (status == Status.ERROR) {
            showSnackbar(getString(R.string.common_msg_api_error)) {
                viewModel.updatePlaylistInfo()
            }
            return
        }
        requireActivity().finish()
    }

    private fun observeStatus(status: Status?) {
        when (status) {
            Status.LOADING -> {
                // do nothing
            }
            Status.SUCCESS -> {
                // do nothing
            }
            Status.ERROR -> run {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.getPlaylist()
                }
            }
        }
    }

    companion object {
        fun newInstance() = EditPlaylistFragment()
    }

}
