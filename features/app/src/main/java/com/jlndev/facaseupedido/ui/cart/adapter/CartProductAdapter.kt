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
                itemQuantityView.text = root.context.getString(R.string.quantity_label, item.quantity.toString())
                itemQuantityView.setOnClickListener {
                    cartProductAdapterListener.updateProductToCart(item)
                }
                itemDeleteProductToCartView.setOnClickListener {
                    cartProductAdapterListener.deleteProductToCart(item)
                }
            }
        }
    }

    fun getProductItems(): List<ProductItem> {
        return items
    }

    fun submitListProductItems(productItems :List<ProductItem>) {
        submitList(productItems)
    }

    fun removeProductItem(productItem: ProductItem){
        removeItem(productItem)
    }

    fun updateProductItem(productItem: ProductItem){
        updateItem(productItem)
    }
}