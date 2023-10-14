package com.jlndev.coreandroid.ext

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan

const val NEGATIVE_VALUE = -1

fun String.setBoldSubstring(substring: String? = null): CharSequence {
    val spannableString = SpannableString(this)

    substring?.let {
        val start = this.indexOf(it)
        val end = start + it.length

        if (start == NEGATIVE_VALUE) {
            return@let
        }

        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            start,
            end,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    } ?: run {
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            this.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    return spannableString
}
