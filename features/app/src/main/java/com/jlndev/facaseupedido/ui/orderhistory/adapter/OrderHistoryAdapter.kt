package com.jlndev.facaseupedido.ui.orderhistory.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jlndev.coreandroid.bases.adapter.BaseAdapterController
import com.jlndev.coreandroid.bases.adapter.BaseAdapterListener
import com.jlndev.coreandroid.bases.adapter.BaseViewHolder
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.ItemOrderHistoryBinding
import com.jlndev.facaseupedido.ui.uitls.model.OrderHistoryItem
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem

class OrderHistoryAdapter(
    private val orderHistoryAdapterListener: OrderHistoryAdapterListener
) : BaseAdapterController<OrderHistoryItem, BaseViewHolder<OrderHistoryItem>, BaseAdapterListener<OrderHistoryItem>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<OrderHistoryItem> {
        val itemBinding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderHistoryViewHolder(itemBinding)
    }

    inner class OrderHistoryViewHolder(private val itemBinding: ItemOrderHistoryBinding) : BaseViewHolder<OrderHistoryItem>(itemBinding.root) {
        override fun bind(item: OrderHistoryItem) {
            with(itemBinding) {
                itemOrderIdView.text = root.context.getString(R.string.order_id, item.id)
                itemOrderTotalPriceView.text = root.context.getString(R.string.order_total_price, item.totalValue.toCurrency())
                itemOrderProductsQuantityView.text  = root.context.getString(R.string.order_quantity, item.quantityItems.toString())
                recyclerProductItemsView.adapter = OrderHistoryProductAdapter(object : BaseAdapterListener<ProductItem>{
                    override fun onAdapterItemClicked(
                        position: Int,
                        item: ProductItem,
                        view: View?
                    ) {
                        orderHistoryAdapterListener.clickedProductItem(item)
                    }
                }).apply {
                    submitList(item.productsItems)
                }

                itemShowProductsImageView.setOnClickListener {
                    showProductItems()
                }

                itemView.setOnClickListener {
                    showProductItems()
                }
            }
        }

        private fun showProductItems() {
            with(itemBinding) {
                if (recyclerProductItemsView.visibility == View.VISIBLE) {
                    itemShowProductsImageView.animate().rotation(0f)
                    recyclerProductItemsView.gone(true)
                } else {
                    itemShowProductsImageView.animate().rotation(180f)
                    recyclerProductItemsView.visible(true)
                }
            }
        }
    }

    interface OrderHistoryAdapterListener {
        fun clickedProductItem(item: ProductItem)
    }
}