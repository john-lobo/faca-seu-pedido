package com.jlndev.coreandroid.bases.adapter

import android.view.View

interface BaseAdapterListener<T> {
    fun onAdapterItemClicked(position: Int, item: T, view: View? = null)
}