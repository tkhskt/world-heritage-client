package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Style
import com.airbnb.paris.annotations.Styleable
import com.github.gericass.world_heritage_client.library.R

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class NewPlaylistView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.library_view_new_playlist, this)
    }

    @CallbackProp
    fun setClickListener(clickListener: () -> Unit) {
        setOnClickListener {
            clickListener()
        }
    }

    companion object {
        @Style(isDefault = true)
        val DEFAULT_STYLE = R.style.library_PlaylistStyle
    }
}