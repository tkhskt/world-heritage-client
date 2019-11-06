package com.github.gericass.world_heritage_client.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image_url")
fun loadImage(v: ImageView, imageUrl: String?) {
    Glide.with(v).load(imageUrl).centerCrop().into(v)
}