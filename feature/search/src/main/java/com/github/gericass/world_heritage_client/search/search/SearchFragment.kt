package com.github.gericass.world_heritage_client.search.search


import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.gericass.world_heritage_client.common.observe
import com.github.gericass.world_heritage_client.data.model.Keyword
import com.github.gericass.world_heritage_client.search.R
import com.github.gericass.world_heritage_client.search.SearchItemKeywordBindingModel_
import com.github.gericass.world_heritage_client.search.databinding.SearchFragmentSearchBinding
import com.github.gericass.world_heritage_client.search.view.KeywordClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private lateinit var binding: SearchFragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private val args: SearchFragmentArgs by navArgs()

    private val listener = object : KeywordClickListener {
        override fun onClick(keyword: String) {
            viewModel.keywordEditText.value = keyword
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.run {
            observe(keywordLog, ::observeKeywords)
            observe(searchButton, ::observeSearchButton)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.search_fragment_search, container, false)
        binding = SearchFragmentSearchBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        lifecycle.addObserver(viewModel)
        args.keyword?.let {
            viewModel.keywordEditText.value = it
        }
        binding.searchText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    transitToResult(viewModel.keywordEditText.value)
                    return true
                }
                return false
            }
        })
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

    private fun observeSearchButton(event: Unit?) {
        transitToResult(viewModel.keywordEditText.value)
    }

    private fun observeKeywords(keyword: List<Keyword>?) {
        val models = keyword?.mapIndexed { i, v ->
            SearchItemKeywordBindingModel_().apply {
                id(i)
                keyword(v.keyword)
                listener(listener)
            }
        } ?: return
        binding.keywordLogRecycler.setModels(models)
    }

    private fun transitToResult(keyword: String?) {
        val word = keyword ?: return
        viewModel.saveKeyword(word)
        val direction = SearchFragmentDirections.searchToResult(word)
        findNavController().navigate(direction)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            args.keyword?.run {
                findNavController().navigateUp()
            } ?: run {
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
