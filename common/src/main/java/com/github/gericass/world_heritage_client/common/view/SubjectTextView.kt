package com.github.gericass.world_heritage_client.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.airbnb.paris.annotations.Style
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.paddingEndDp
import com.airbnb.paris.extensions.paddingStartDp
import com.airbnb.paris.extensions.subjectTextViewStyle
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

    companion object {
        @Style(isDefault = true)
        val NO_PADDING_STYLE = subjectTextViewStyle {
            paddingStartDp(0)
            paddingEndDp(0)
        }
        @Style(isDefault = true)
        val PADDING_STYLE = subjectTextViewStyle {
            paddingStartDp(16)
            paddingEndDp(16)
        }
    }
}