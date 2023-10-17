package com.jlndev.facaseupedido.ui.uitls.model

import com.jlndev.coreandroid.bases.adapter.BaseDiffItemView

data class ProductItem(
    override val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val image: String,
    var quantity: Int = 1
): BaseDiffItemView()