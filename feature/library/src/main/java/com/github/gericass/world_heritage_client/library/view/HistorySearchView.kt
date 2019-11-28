package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Styleable
import com.github.gericass.world_heritage_client.library.R
import kotlinx.android.synthetic.main.library_view_history_search.view.*

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HistorySearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.library_view_history_search, this)
        val color = ContextCompat.getColor(getContext(), R.color.common_gray_background)
        setBackgroundColor(color)
        search_edit_text.setOnFocusChangeListener { v, focused ->
            if (focused) v.callOnClick()
        }
        search_motion_layout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

            }

            override fun onTransitionCompleted(p0: MotionLayout?, state: Int) {
                if (state == search_motion_layout.startState) {
                    search_edit_text.clearFocus()
                }
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }
        })


    }

    companion object {
        //@Style(isDefault = true)
        //val DEFAULT_STYLE = R.style.library_SearchTextStyle
    }
}