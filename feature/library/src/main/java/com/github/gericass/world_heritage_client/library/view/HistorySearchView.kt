package com.github.gericass.world_heritage_client.library.view

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.ModelView
import com.airbnb.paris.annotations.Styleable
import com.github.gericass.world_heritage_client.library.R

@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class HistorySearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr) {

    init {
        inflate(context, R.layout.library_view_history_search, this)
        val color = ContextCompat.getColor(getContext(), R.color.common_gray_background)
        setBackgroundColor(color)
        loadLayoutDescription(R.xml.library_motion_cancel)
        //setOnClickListener {
        //    if (currentState == startState) {
        //        Timber.d(currentState.toString())
        //        transitionToEnd()
        //        updateState()
        //        requestLayout()
        //        Timber.d(currentState.toString())
        //    } else {
        //        transitionToStart()
        //    }
//
        //}
    }

    companion object {
        //@Style(isDefault = true)
        //val DEFAULT_STYLE = R.style.library_SearchTextStyle
    }
}