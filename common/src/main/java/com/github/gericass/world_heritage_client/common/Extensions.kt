package com.github.gericass.world_heritage_client.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) {
    liveData.observe(this, Observer(body))
}

fun BaseFragment.showSnackbar(
    msg: String,
    length: Int = BaseTransientBottomBar.LENGTH_INDEFINITE
) {
    val snackbar = Snackbar.make(requireView(), msg, length).apply {
        setAction("Retry") {
            refresh()
            dismiss()
        }
    }
    val observer = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
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

fun Fragment.hideKeyboard(rootView: View) {
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(rootView.windowToken, 0)
}

fun Fragment.showKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}