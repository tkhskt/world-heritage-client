package com.github.gericass.world_heritage_client.common.dialog.sheet

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.github.gericass.world_heritage_client.common.R
import com.github.gericass.world_heritage_client.common.databinding.CommonFragmentBottomSheetBinding
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.view.PlaylistCheckBoxView
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class BottomSheetFragment : BottomSheetDialogFragment(), CoroutineScope {

    private val viewModel: BottomSheetViewModel by viewModel()

    private lateinit var binding: CommonFragmentBottomSheetBinding

    override val coroutineContext: CoroutineContext = Job() + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(playlists, ::observePlaylists)
        }
        lifecycle.addObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.common_fragment_bottom_sheet, container, false)
        binding = CommonFragmentBottomSheetBinding.bind(view)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@BottomSheetFragment
            viewModel = this@BottomSheetFragment.viewModel
        }
    }

    private fun observePlaylists(playlists: List<Playlist>?) {
        playlists?.forEach { playlist ->
            val checkBox = PlaylistCheckBoxView(requireContext())
                .apply {
                    setTitle(playlist.title)
                    setCheckedListener { _, isChecked ->
                        if (isChecked) {
                            viewModel.checkedList.add(playlist.id)

                        } else {
                            viewModel.checkedList.remove(playlist.id)
                        }
                    }
                }
            binding.checkboxContainer.addView(checkBox)
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        coroutineContext.cancel()
    }

    companion object {
        suspend fun showWithResult(fm: FragmentManager) =
            suspendCoroutine<List<Int>?> { cont ->
                val fragment = BottomSheetFragment()
                fragment.show(fm, "tag")
                fragment.launch {
                    val playlistIds = fragment.viewModel.buttonChannel.receive()
                    cont.resume(playlistIds)
                    fragment.dismiss()
                }
            }

        const val CREATE_PLAYLIST = 0
    }

}