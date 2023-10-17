package com.jlndev.facaseupedido.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jlndev.coreandroid.bases.adapter.BaseAdapterController
import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.coreandroid.bases.adapter.BaseViewHolder
import com.jlndev.coreandroid.ext.loadImage
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.facaseupedido.databinding.ItemProductBinding
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

class ProductAdapter(
    private val productAdapterListener: ProductAdapterListener
) : BaseAdapterController<ProductItem, BaseViewHolder<ProductItem>, BaseAdapterListener<ProductItem>>(productAdapterListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductItem> {
        val itemBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(itemBinding)
    }

    inner class ProductViewHolder(private val itemBinding: ItemProductBinding) :
        BaseViewHolder<ProductItem>(itemBinding.root) {
        override fun bind(item: ProductItem) {
            with(itemBinding) {
                itemTitleView.text = item.title
                itemPriceView.text = item.price.toCurrency()
                itemImageview.loadImage(item.image)
                addCartView.setOnClickListener {
                    productAdapterListener.addProcutToCart(item)
                }
            }
        }
    }

    interface ProductAdapterListener : BaseAdapterListener<ProductItem> {
        fun addProcutToCart(productItem : ProductItem)
    }
}
