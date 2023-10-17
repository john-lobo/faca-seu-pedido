package com.jlndev.facaseupedido.ui.uitls.model

import android.os.Parcelable
import com.jlndev.coreandroid.bases.adapter.BaseDiffItemView
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductItem(
    override val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Int = 1
): Parcelable, BaseDiffItemView()