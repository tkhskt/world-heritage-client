package com.github.gericass.world_heritage_client.common

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("image_url")
fun loadImage(v: ImageView, imageUrl: String?) {
    Glide.with(v).load(imageUrl).centerCrop().into(v)
}

@BindingAdapter("duration")
fun duration(v: TextView, duration: Double) {
    val hour = (duration / 3600).toInt()
    val minute = ((duration % 3600) / 60).toInt()
    v.text = "%02d:%02d".format(hour, minute)
}

@SuppressLint("RestrictedApi")
@BindingAdapter("selection")
fun setSelection(view: EditText, str: CharSequence?) {
    str?.let {
        view.setSelection(it.length)
    }
}