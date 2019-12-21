package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Style
import com.airbnb.paris.annotations.Styleable
import com.bumptech.glide.Glide
import com.github.gericass.world_heritage_client.data.model.PlayList
import com.github.gericass.world_heritage_client.library.R

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PlayListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val image: ImageView
    private val name: TextView
    private lateinit var playList: PlayList

    init {
        inflate(context, R.layout.library_view_playlist, this)
        image = findViewById(R.id.thumbnail)
        name = findViewById(R.id.name)
    }

    @ModelProp
    fun setPlayList(playList: PlayList) {
        this.playList = playList
    }

    @AfterPropsSet
    fun setUp() {
        name.text = playList.title
        playList.thumbnailImg?.let {
            Glide.with(image)
                .load(it)
                .into(image)
        } ?: run {
            Glide.with(image)
                .load(playList.thumbnailImgUrl)
                .into(image)
        }
    }

    @CallbackProp
    fun setClickListener(clickListener: (PlayList) -> Unit) {
        setOnClickListener {
            clickListener(playList)
        }
    }

    companion object {
        @Style(isDefault = true)
        val DEFAULT_STYLE = R.style.library_EditTextStyle
    }
}