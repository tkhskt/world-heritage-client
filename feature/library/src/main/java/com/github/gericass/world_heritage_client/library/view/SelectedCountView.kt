package com.github.gericass.world_heritage_client.library.view


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.github.gericass.world_heritage_client.library.databinding.LibraryViewSelectedCountBinding
import com.github.gericass.world_heritage_client.library.playlist.create.CreatePlaylistViewModel

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SelectedCountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = LibraryViewSelectedCountBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    ).apply {
        lifecycleOwner = context as LifecycleOwner
    }

    @ModelProp
    fun setViewModel(viewModel: CreatePlaylistViewModel) {
        binding.viewModel = viewModel
    }
}