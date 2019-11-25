package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.github.gericass.world_heritage_client.library.R

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class PlayListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val image: ImageView
    private val name: TextView

    init {
        inflate(context, R.layout.library_view_playlist, this)
        image = findViewById(R.id.thumbnail)
        name = findViewById(R.id.name)
    }


    @ModelProp
    fun setPlayList(repoName: CharSequence) {
        this.repoName.text = repoName
    }


    @CallbackProp
    fun setClickListener(clickListener: View.OnClickListener?) {
        setOnClickListener(clickListener)
    }
}