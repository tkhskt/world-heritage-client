package com.github.gericass.world_heritage_client.common

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("image_url")
fun loadImage(v: ImageView, imageUrl: String?) {
    Glide.with(v)
        .load(imageUrl)
        .centerCrop()
        .thumbnail(Glide.with(v).load(R.drawable.common_logo).centerCrop())
        .into(v)
}

@BindingAdapter("duration")
fun duration(v: TextView, duration: Double) {
    val minute = (duration / 60).toInt()
    val second = (duration % 60).toInt()
    v.text = "%02d:%02d".format(minute, second)
}

@SuppressLint("RestrictedApi")
@BindingAdapter("selection")
fun setSelection(view: EditText, str: CharSequence?) {
    str?.let {
        view.setSelection(it.length)
    }
}