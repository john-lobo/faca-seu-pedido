package com.jlndev.facaseupedido.ui.cart

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.MainActivity
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentCartBinding
import com.jlndev.facaseupedido.ui.cart.adapter.CartProductAdapter
import com.jlndev.facaseupedido.ui.uitls.components.QuantityInputDialog
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItem
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItemModel
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>() {

    override val viewModel: CartViewModel by viewModel()
    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onInitData() {
        viewModel.getProductsItems()
    }

    override fun onGetViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        cartProductAdapter = CartProductAdapter(object : CartProductAdapter.CartProductAdapterListener {
            override fun onAdapterItemClicked(position: Int, item: ProductItem, view: View?) {
            }

            override fun updateProcutToCart(productItem: ProductItem) {
                QuantityInputDialog(requireContext(), productItem.quantity).show { quantity ->
                    if(quantity > 0) {
                        productItem.quantity = quantity
                        viewModel.updateQuantityProductItem(productItem.toProductItemModel())
                    } else {
                        viewModel.deleteProductItem(productItem.toProductItemModel())
                    }
                }
            }

            override fun deleteProcutToCart(productItem: ProductItem) {
                showConfirmDeletionProduct(productItem)
            }

            override fun totalValueProductToCart(totalValue: Double) {
                val totalItemsInCart = cartProductAdapter.getProductItems().sumOf { it.quantity }
                binding.confirmOrderView.totalValueView.text = totalValue.toCurrency()
                binding.confirmOrderView.quantityValueView.text = getString(R.string.total_items_in_cart, totalItemsInCart)
            }
        })

        with(binding) {
            recyclerCartProductItemsView.adapter = cartProductAdapter
            notProductsCartView.retryButton.setOnClickListener {
                navToHome()
            }
            confirmOrderView.confirmOrderView.setOnClickListener {
                showConfirmationDialog(binding.confirmOrderView.totalValueView.text.toString(), cartProductAdapter.getProductItems().sumOf { it.quantity })
            }
        }
    }

    override fun onInitViewModel() {
        with(viewModel) {
            productsItemsLive.observe(viewLifecycleOwner) {
                when(it) {
                    is ResponseState.Success -> {
                        val items = it.data.map { it.toProductItem() }
                        cartProductAdapter.submiListProductItems(items)
                        if(items.isNotEmpty()) {
                            showView()
                        } else {
                            showViewProductNotFound()
                        }
                    }
                    is ResponseState.Error -> {
                        showErrorView()
                    }
                }
            }

            updateQuantityProductToCartLive.observe(viewLifecycleOwner) {
                it?.let {
                    when(it) {
                        is ResponseState.Success -> {
                            cartProductAdapter.updateProductItem(it.data.toProductItem())
                            requireView().showSnackbar(getString(R.string.updated_quantity_product))
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.updated_quantity_product_error)) {
                                it.action?.invoke()
                            }
                        }
                    }
                }
            }

            deleteProductToCartLive.observe(viewLifecycleOwner) {
                it?.let {
                    when(it) {
                        is ResponseState.Success -> {
                            cartProductAdapter.removeProductItem(it.data.toProductItem())
                            requireView().showSnackbar(getString(R.string.deleted_product))
                            if(cartProductAdapter.getProductItems().isEmpty()) {
                                showViewProductNotFound()
                            }
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.deleted_product_error)) {
                                it.action?.invoke()
                            }
                        }
                    }
                }
            }

            createOrderLive.observe(viewLifecycleOwner) {
                it?.let {
                    when(it) {
                        is ResponseState.Success -> {
                            requireView().showSnackbar(getString(R.string.create_order))
                            cartProductAdapter.submiListProductItems(listOf())
                            showViewProductNotFound()
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.create_order_error)) {
                                it.action?.invoke()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun showLoading() {
        with(binding) {
            recyclerCartProductItemsView.gone()
            confirmOrderView.root.gone()
            errorView.root.gone()
            notProductsCartView.root.gone()
            loadingView.visible()
        }
    }

    override fun hideLoading() {
        binding.loadingView.gone()
    }

    override fun showView() {
        with(binding) {
            errorView.root.gone()
            notProductsCartView.root.gone()
            confirmOrderView.root.visible()
            recyclerCartProductItemsView.visible()
            showClearList(true)
        }
    }

    private fun showViewProductNotFound() {
        with(binding) {
            recyclerCartProductItemsView.gone()
            confirmOrderView.root.gone()
            errorView.root.gone()
            notProductsCartView.root.visible()
            showClearList(false)
        }
    }

    override fun showErrorView() {
        with(binding) {
            confirmOrderView.root.gone()
            notProductsCartView.root.gone()
            recyclerCartProductItemsView.gone()
            errorView.root.visible()
            errorView.retryButton.setOnClickListener {
                viewModel.getProductsItems()
            }
        }
    }

    private fun showConfirmDeletionProduct(productItem: ProductItem) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_to_deleted_product))
        builder.setMessage(getString(R.string.Do_you_want_to_delete_this_product_from_the_cart))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            viewModel.deleteProductItem(productItem.toProductItemModel())
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showConfirmClearCart() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirm_clear_cart))
        builder.setMessage(getString(R.string.do_you_want_to_delete_alL_products_from_the_cart))

        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            viewModel.deleteAllProductsItems()
            dialog.dismiss()
        }

        builder.setNegativeButton(getString(R.string.no)) { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun showConfirmationDialog(totalValue: String, quantityItems: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.confirmation_order))
        val message = getString(R.string.confirm_order_message, totalValue, quantityItems)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.confirm_order)) { dialog, which ->
            viewModel.insertOrder(cartProductAdapter.getProductItems().map { it.toProductItemModel() })
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    override fun onResume() {
        super.onResume()
        showClearList(true)
    }

    override fun onPause() {
        super.onPause()
        showClearList(false)
    }

    private fun showClearList(show: Boolean) {
        val toolbar = (requireActivity() as MainActivity).findViewById<Toolbar>(R.id.toolbar)
        toolbar.findViewById<ImageView>(R.id.clearCartView).apply {
            setOnClickListener {
                showConfirmClearCart()
            }

            if(cartProductAdapter.getProductItems().isNotEmpty() && show) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun navToHome() {
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }
}