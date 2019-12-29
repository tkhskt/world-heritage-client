package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.github.gericass.world_heritage_client.common.spinner.VideoSpinnerAdapter
import com.github.gericass.world_heritage_client.common.view.VideoClickListener
import com.github.gericass.world_heritage_client.common.vo.SpinnerItem
import com.github.gericass.world_heritage_client.data.model.Videos
import com.github.gericass.world_heritage_client.library.R
import com.github.gericass.world_heritage_client.library.databinding.LibraryViewVideoBinding


@ModelView(autoLayout = ModelView.Size.WRAP_WIDTH_WRAP_HEIGHT)
class LibraryVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding: LibraryViewVideoBinding

    private lateinit var spinnerItems: List<SpinnerItem>

    private val spinnerListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(spinnerView: AdapterView<*>?, view: View?, i: Int, j: Long) {
            val spinner = spinnerView as Spinner
            val item = spinner.selectedItem as SpinnerItem
            item.action()
        }

        override fun onNothingSelected(spinnerView: AdapterView<*>?) {
            // no nothing
        }
    }

    init {
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.library_view_video, this, true)
    }

    @CallbackProp
    fun setListener(listener: VideoClickListener?) {
        binding.listener = listener
    }

    @ModelProp
    fun setVideo(video: Videos.Video) {
        binding.video = video
    }

    @ModelProp
    @JvmOverloads
    fun setSpinnerItems(items: List<SpinnerItem> = emptyList()) {
        spinnerItems = items
    }

    @AfterPropsSet
    fun setUp() {
        val spinnerAdapter = VideoSpinnerAdapter(context, spinnerItems).apply {
            setDropDownViewResource(R.layout.common_view_spinner_item)
        }
        binding.spinner.apply {
            adapter = spinnerAdapter
            setSelection(0, false)
            onItemSelectedListener = spinnerListener
        }
        binding.button.setOnClickListener {
            binding.spinner.performClick()
        }
    }
}
