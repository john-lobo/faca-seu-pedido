package com.jlndev.facaseupedido.ui.orderhistory.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jlndev.coreandroid.bases.adapter.BaseAdapterController
import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.coreandroid.bases.adapter.BaseViewHolder
import com.jlndev.coreandroid.ext.loadImage
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.ItemProductOrderHistoryBinding
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

class OrderHistoryProductAdapter(
    listener: BaseAdapterListener<ProductItem>
) : BaseAdapterController<ProductItem, BaseViewHolder<ProductItem>, BaseAdapterListener<ProductItem>>(listener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductItem> {
        val itemBinding = ItemProductOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHistoryProductViewHolder(itemBinding)
    }

    inner class OrderHistoryProductViewHolder(private val itemBinding: ItemProductOrderHistoryBinding) :
        BaseViewHolder<ProductItem>(itemBinding.root) {
        override fun bind(item: ProductItem) {
            with(itemBinding) {
                itemTitleView.text = item.title
                itemPriceView.text = item.price.toCurrency()
                itemImageview.loadImage(item.image)
                itemQuantityView.text = root.context.getString(R.string.quantity_label, item.quantity)
            }
        }
    }
}