package com.github.gericass.world_heritage_client.search.result


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.trusted.TrustedWebActivityIntentBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.search.R
import com.github.gericass.world_heritage_client.search.databinding.SearchFragmentResultBinding
import com.google.androidbrowserhelper.trusted.TwaLauncher
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultFragment : BaseFragment() {

    private lateinit var binding: SearchFragmentResultBinding

    private val viewModel: ResultViewModel by viewModel()

    private val args: ResultFragmentArgs by navArgs()

    private val videoClickListener = object : VideoClickListener {
        override fun onClick(video: Videos.Video) {
            val builder = TrustedWebActivityIntentBuilder(Uri.parse(video.video_url))
            TwaLauncher(requireContext()).launch(builder, null, null)
        }
    }

    private val resultController = ResultController(videoClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(pagedList, ::observePagedList)
            observe(networkStatus, ::observeNetworkStatus)
            observe(keywordClick, ::observeKeywordClick)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_fragment_result, container, false)
        binding = SearchFragmentResultBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        binding.keywordLogRecycler.setController(resultController)
        binding.viewModel = viewModel
        viewModel.keyword.value = args.keyword
        lifecycle.addObserver(viewModel)
    }

    private fun setUpToolbar() {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(binding.searchToolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                setHomeButtonEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    private fun observeKeywordClick(event: Unit?) {
        viewModel.keyword.value?.let {
            val action = ResultFragmentDirections.resultToSearch(it)
            findNavController().navigate(action)
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        resultController.submitList(pagedList)
    }

    private fun observeNetworkStatus(status: Status?) {
        when (status ?: return) {
            Status.LOADING -> resultController.isLoading = true
            Status.SUCCESS -> {
                resultController.isLoading = false
            }
            Status.ERROR -> run {
                binding.root.showSnackbar(getString(R.string.common_msg_api_error))
                resultController.isLoading = false
            }
        }
    }

    override fun refresh() {
        // TODO
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
