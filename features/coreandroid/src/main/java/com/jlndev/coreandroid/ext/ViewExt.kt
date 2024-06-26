package com.jlndev.coreandroid.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar

fun View.visible(animate: Boolean = false) {
    if(animate) {
        val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        fadeIn.duration = 300
        fadeIn.addUpdateListener { _ ->
            visibility = View.VISIBLE
        }
        fadeIn.start()
    } else {
        visibility = View.VISIBLE
    }
}

fun View.gone(animate: Boolean = false) {
    if(animate) {
        val fadeOut = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
        fadeOut.duration = 300
        fadeOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                visibility = View.GONE
            }
        })
        fadeOut.start()
    } else {
        visibility = View.GONE
    }
}

fun View.showSnackbar(
    message: String,
    actionText: String? = null,
    view: View? = null,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    if (actionText != null && action != null) {
        snackbar.setAction(actionText) {
            action()
            snackbar.dismiss()
        }
    }
    snackbar.setAnchorView(view)
    snackbar.show()
}

fun FragmentActivity.showSnackbar(
    message: String,
    actionText: String? = null,
    action: (() -> Unit)? = null
) {
    val rootView = findViewById<View>(android.R.id.content)
    val snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
    if (actionText != null && action != null) {
        snackbar.setAction(actionText) {
            action()
            snackbar.dismiss()
        }
    }
    snackbar.show()
}


fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}