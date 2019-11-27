package com.github.gericass.world_heritage_client.common.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.airbnb.paris.annotations.Style
import com.airbnb.paris.annotations.Styleable
import com.github.gericass.world_heritage_client.common.R


@Styleable
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class SubjectTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private val subject: TextView

    init {
        inflate(context, R.layout.common_view_subject_text, this)
        subject = findViewById(R.id.subject)
    }

    @TextProp
    fun setText(subject: CharSequence) {
        this.subject.text = subject
    }

    @JvmOverloads
    @ModelProp
    fun sectionStyle(flag: Boolean = false) {
        if (flag) {
            subject.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
                setTypeface(typeface, Typeface.NORMAL)
            }
        } else {
            subject.apply {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
                setTypeface(typeface, Typeface.BOLD)
            }
        }
    }

    companion object {
        @Style(isDefault = true)
        val NO_PADDING_STYLE = R.style.common_NoPaddingStyle

        @Style(isDefault = true)
        val PADDING_STYLE = R.style.common_PaddingStyle
    }
}