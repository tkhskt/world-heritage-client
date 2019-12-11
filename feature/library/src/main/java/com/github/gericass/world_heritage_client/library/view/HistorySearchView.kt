package com.github.gericass.world_heritage_client.library.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Styleable
import com.github.gericass.world_heritage_client.common.hideKeyboard
import com.github.gericass.world_heritage_client.common.vo.Event
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryViewHistorySearchBinding
import com.github.gericass.world_heritage_client.library.history.ViewingHistoryViewModel
import timber.log.Timber

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
@SuppressLint("ClickableViewAccessibility")
class HistorySearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var viewModel: ViewingHistoryViewModel

    private val binding = LibraryViewHistorySearchBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).apply {
        lifecycleOwner = context as LifecycleOwner
    }

    init {
        Timber.d("aaaaaa")
        val color = ContextCompat.getColor(getContext(), R.color.common_gray_background)
        setBackgroundColor(color)
        binding.searchEditText.apply {
            setOnFocusChangeListener { v, focused ->
                if (focused) v.callOnClick()
            }
            setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.searchButton.value = Event(v.text.toString())
                        context.hideKeyboard(this@HistorySearchView)
                        if (viewModel.keyword.value.isNullOrEmpty()) {
                            binding.searchMotionLayout.transitionToStart()
                        }
                        binding.searchEditText.clearFocus()
                        return true
                    }
                    return false
                }
            })
        }
        binding.cancelButton.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                viewModel.keyword.value = ""
                viewModel.searchButton.value = Event("")
                binding.searchEditText.clearFocus()
                context.hideKeyboard(this@HistorySearchView)
            }
            false
        }
    }

    @ModelProp
    fun setViewModel(viewModel: ViewingHistoryViewModel) {
        this.viewModel = viewModel
    }

    @JvmOverloads
    @ModelProp
    fun refresh(isRefreshing: Boolean = true) {
        if (isRefreshing) {
            binding.run {
                searchMotionLayout.transitionToStart()
            }
        }
    }

    @AfterPropsSet
    fun setUp() {
        binding.viewModel = viewModel
        val keyword = viewModel.keyword.value
        if (viewModel.currentKeyword != keyword) {
            viewModel.keyword.value = viewModel.currentKeyword
            if (viewModel.currentKeyword.isNullOrEmpty()) {
                binding.searchMotionLayout.transitionToStart()
                return
            }
            binding.searchMotionLayout.transitionToEnd()
        } else if (viewModel.currentKeyword != null) {
            binding.searchMotionLayout.transitionToEnd()
        }
    }
}