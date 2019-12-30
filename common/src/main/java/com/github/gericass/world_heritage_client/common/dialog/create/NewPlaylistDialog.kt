package com.github.gericass.world_heritage_client.common.dialog.create

import android.app.AlertDialog
import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.github.gericass.world_heritage_client.common.R
import com.github.gericass.world_heritage_client.common.dp

class NewPlaylistDialog {

    companion object {
        private fun AlertDialog.addEditTextListener(editText: EditText): AlertDialog {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = !s.isNullOrBlank()
                }
            })
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                }

            }
            return this
        }

        fun show(context: Context, callback: (String) -> Unit) {
            val editText = EditText(context).apply {
                inputType = InputType.TYPE_CLASS_TEXT
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val margin = 16.dp
                    setMargins(margin, margin, margin, 0)
                }
                setTextColor(ContextCompat.getColor(context, R.color.common_gray_dark))
                hint = "タイトル"
                setHintTextColor(ContextCompat.getColor(context, R.color.common_gray))
            }
            val frameLayout = FrameLayout(context).apply {
                addView(editText)
            }
            AlertDialog.Builder(context, R.style.common_AlertDialogStyle)
                .setTitle("新しい再生リスト")
                .setView(frameLayout)
                .setNegativeButton("キャンセル", null)
                .setPositiveButton("作成") { _, _ ->
                    callback(editText.text.toString())
                }
                .create()
                .addEditTextListener(editText)
                .run {
                    show()
                    getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                }

        }
    }
}

