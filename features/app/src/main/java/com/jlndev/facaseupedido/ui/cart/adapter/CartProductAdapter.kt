package com.jlndev.facaseupedido.ui.cart.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.jlndev.coreandroid.bases.adapter.BaseAdapterController
import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.coreandroid.bases.adapter.BaseViewHolder
import com.jlndev.coreandroid.ext.loadImage
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.ItemCartProductBinding
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

class CartProductAdapter(
    private val cartProductAdapterListener: CartProductAdapterListener
) : BaseAdapterController<ProductItem, BaseViewHolder<ProductItem>, BaseAdapterListener<ProductItem>>(cartProductAdapterListener) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductItem> {
        val itemBinding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartProductViewHolder(itemBinding)
    }

    inner class CartProductViewHolder(private val itemBinding: ItemCartProductBinding) :
        BaseViewHolder<ProductItem>(itemBinding.root) {
        override fun bind(item: ProductItem) {
            with(itemBinding) {
                itemTitleView.text = item.title
                itemPriceView.text = item.price.toCurrency()
                itemImageview.loadImage(item.image)
                itemQuantityView.text = root.context.getString(R.string.quantity_label, item.quantity)
                itemQuantityView.setOnClickListener {
                    cartProductAdapterListener.updateProcutToCart(item)
                }
                itemDeleteProductToCartView.setOnClickListener {
                    cartProductAdapterListener.deleteProcutToCart(item)
                }
            }
        }
    }

    fun getProductItems(): List<ProductItem> {
        return items
    }

    fun submiListProductItems(productItems :List<ProductItem>) {
        submitList(productItems)
        cartProductAdapterListener.totalValueProductToCart(items.sumOf { it.price * it.quantity })
    }

    fun removeProductItem(productItem: ProductItem){
        removeItem(productItem)
        cartProductAdapterListener.totalValueProductToCart(items.sumOf { it.price * it.quantity })
    }

    fun updateProductItem(productItem: ProductItem){
        updateItem(productItem)
        cartProductAdapterListener.totalValueProductToCart(items.sumOf { it.price * it.quantity })
    }

    fun clearProducts() {
        clear()
        cartProductAdapterListener.totalValueProductToCart(items.sumOf { it.price * it.quantity })
    }

    interface CartProductAdapterListener : BaseAdapterListener<ProductItem> {
        fun updateProcutToCart(productItem : ProductItem)
        fun deleteProcutToCart(productItem : ProductItem)
        fun totalValueProductToCart(totalValue: Double)
    }
}