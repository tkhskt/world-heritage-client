package com.github.gericass.world_heritage_client.common.view

import android.content.Context
import android.util.AttributeSet
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.LinearLayout
import com.github.gericass.world_heritage_client.common.R

class PlaylistCheckBoxView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val checkBox: CheckBox

    init {
        inflate(context, R.layout.common_view_checkbox, this)
        checkBox = findViewById(R.id.checkbox)
    }

    fun setTitle(title: String) {
        checkBox.text = title
    }

    fun setCheckedListener(action: (CompoundButton, Boolean) -> Unit) {
        checkBox.setOnCheckedChangeListener { v, isChecked ->
            action(v, isChecked)
        }
    }

}