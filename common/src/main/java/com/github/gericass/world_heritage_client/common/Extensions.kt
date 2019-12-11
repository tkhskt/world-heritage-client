package com.github.gericass.world_heritage_client.common

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.Period
import org.threeten.bp.ZoneId
import java.util.*


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun Fragment.showSnackbar(
    msg: String,
    length: Int = BaseTransientBottomBar.LENGTH_INDEFINITE,
    refresh: () -> Unit
) {
    val snackbar = Snackbar.make(requireView(), msg, length).apply {
        setAction("Retry") {
            refresh()
            dismiss()
        }
    }
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun dissmiss() {
            snackbar.dismiss()
        }
    }
    lifecycle.addObserver(observer)
    snackbar.apply {
        addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                lifecycle.removeObserver(observer)
            }
        })
        show()
    }
}

fun Context.hideKeyboard(rootView: View) {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(rootView.windowToken, 0)
}

fun Context.showKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Date.period(toDate: Date? = null): Period {
    val zone = ZoneId.systemDefault()
    val from = Instant.ofEpochMilli(this.time)
        .atZone(zone)
        .toLocalDate()
    val to = toDate?.run {
        Instant.ofEpochMilli(time)
            .atZone(zone)
            .toLocalDate()
    } ?: LocalDate.now(zone)
    return Period.between(from, to)
}