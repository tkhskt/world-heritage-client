package com.github.gericass.world_heritage_client.common

import android.annotation.SuppressLint
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import org.threeten.bp.*


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

@BindingAdapter("view_number")
fun viewNumber(v: TextView, number: Int) {
    val count = if (number >= 10000) {
        "${number / 10000}万"
    } else {
        number.toString()
    }
    v.text = v.context.getString(R.string.common_view_number, count)
}


@BindingAdapter("post_date")
fun postDate(v: TextView, timestamp: Int) {
    val zone = ZoneId.systemDefault()
    val dateTime = Instant
        .ofEpochMilli(timestamp * 1000L)
        .atZone(zone)
        .toLocalDateTime()
    val now = LocalDateTime.now(zone)
    val period = Period.between(dateTime.toLocalDate(), now.toLocalDate())

    val duration = Duration.between(dateTime, now)

    val periodText = when {
        period.years > 0 -> "${period.years}年"
        period.months > 0 -> "${period.months}か月"
        period.days > 6 -> "${period.days / 7}週間"
        period.days > 0 -> "${period.days}日"
        duration.toHours() > 0 -> "${duration.toHours()}時間"
        duration.toMinutes() > 0 -> "${duration.toMinutes()}分"
        else -> "${duration.seconds}秒"
    }
    v.text = v.context.getString(R.string.common_post_date, periodText)
}