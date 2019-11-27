package com.github.gericass.world_heritage_client.search.result


import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyRecyclerView
import com.github.gericass.world_heritage_client.common.BaseFragment
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.common.showSnackbar
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.common.vo.Status
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.search.R
import com.github.gericass.world_heritage_client.search.databinding.SearchFragmentResultBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ResultFragment : BaseFragment() {

    private lateinit var binding: SearchFragmentResultBinding

    private val viewModel: ResultViewModel by viewModel()

    private val args: ResultFragmentArgs by navArgs()

    private val resultController = ResultController(videoClickListener)

    override val recyclerView: EpoxyRecyclerView by lazy { binding.keywordLogRecycler }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(pagedList, ::observePagedList)
            observe(networkStatus, ::observeNetworkStatus)
            observe(keywordClick, ::observeKeywordClick)
        }
        viewModel.saveKeyword(args.keyword)
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
        // 画面回転によるPagedListの再生成(APIへの再リクエスト)を防ぐ
        if (viewModel.keyword.value.isNullOrEmpty()) {
            viewModel.keyword.value = args.keyword
        }
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

    private fun observeKeywordClick(event: Event<String>?) {
        event?.getContentIfNotHandled()?.let { keyword ->
            val action = ResultFragmentDirections.resultToSearch(keyword)
            findNavController().navigate(action)
        }
    }

    private fun observePagedList(pagedList: PagedList<Videos.Video>?) {
        resultController.submitList(pagedList)
    }

    private fun observeNetworkStatus(status: Status?) {
        when (status ?: return) {
            Status.LOADING -> resultController.isLoading = true
            Status.SUCCESS -> resultController.isLoading = false
            Status.ERROR -> run {
                showSnackbar(getString(R.string.common_msg_api_error)) {
                    viewModel.fetch()
                }
                resultController.isLoading = false
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            requireActivity().finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
