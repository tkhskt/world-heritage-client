package com.github.gericass.world_heritage_client.library


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.gericass.world_heritage_client.library.databinding.LibraryFragmentLibraryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class LibraryFragment : Fragment() {

    private val viewModel: LibraryViewModel by viewModel()

    private lateinit var binding: LibraryFragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.library_fragment_library, container, false)
        binding = LibraryFragmentLibraryBinding.bind(view)
        return binding.root
    }


}
