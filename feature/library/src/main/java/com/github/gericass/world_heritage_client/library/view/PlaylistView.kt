package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Style
import com.airbnb.paris.annotations.Styleable
import com.bumptech.glide.Glide
import com.github.gericass.world_heritage_client.data.model.Playlist
import com.github.gericass.world_heritage_client.library.R

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PlaylistView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val image: ImageView
    private val icon: ImageView
    private val name: TextView
    private lateinit var playlist: Playlist

    init {
        inflate(context, R.layout.library_view_playlist, this)
        image = findViewById(R.id.thumbnail)
        icon = findViewById(R.id.icon)
        name = findViewById(R.id.name)
    }

    @ModelProp
    fun setPlaylist(playlist: Playlist) {
        this.playlist = playlist
    }

    @AfterPropsSet
    fun setUp() {
        name.text = playlist.title

        playlist.thumbnailImg?.let {
            image.visibility = View.INVISIBLE
            val drawable = ContextCompat.getDrawable(context, it)?.apply {
                val tint = ContextCompat.getColor(context, R.color.common_gray)
                DrawableCompat.setTint(this, tint)
            }
            Glide.with(icon)
                .load(drawable)
                .into(icon)
        } ?: run {
            icon.visibility = View.INVISIBLE
            Glide.with(image)
                .load(playlist.thumbnailImgUrl)
                .centerCrop()
                .into(image)
        }
    }

    @CallbackProp
    fun setClickListener(clickListener: (Playlist) -> Unit) {
        setOnClickListener {
            clickListener(playlist)
        }
    }

    companion object {
        @Style(isDefault = true)
        val DEFAULT_STYLE = R.style.library_PlaylistStyle
    }
}