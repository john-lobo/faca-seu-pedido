package com.jlndev.coreandroid.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.visible(animate: Boolean = false) {
    if(animate) {
        val fadeIn = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f)
        fadeIn.duration = 150
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
        fadeOut.duration = 150
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
    action: (() -> Unit)? = null,
    view: View? = null
) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    if (actionText != null && action != null) {
        snackbar.setAction(actionText) {
            action()
        }
    }
    snackbar.setAnchorView(view)
    snackbar.show()
}