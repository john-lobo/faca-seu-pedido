package com.jlndev.coreandroid.ext

import android.widget.ProgressBar

fun ProgressBar.showLoading(isLoading: Boolean) {
    if (isLoading) {
        visible()
    } else {
        gone()
    }
}



