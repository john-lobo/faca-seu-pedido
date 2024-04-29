package com.jlndev.facaseupedido.ui.cart

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.NewMainActivity
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentCartBinding
import com.jlndev.facaseupedido.ui.cart.adapter.CartProductAdapter
import com.jlndev.facaseupedido.ui.cart.adapter.CartProductAdapterListener
import com.jlndev.facaseupedido.ui.item.DetailsFragment
import com.jlndev.facaseupedido.ui.item.DetailsFragment.Companion.KEY_SHOW_BUTTON
import com.jlndev.facaseupedido.ui.uitls.components.QuantityInputDialog
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItem
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItemModel
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : BaseFragment<FragmentCartBinding, CartViewModel>() {

    override val viewModel: CartViewModel by viewModel()
    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onInitData() {
        viewModel.getUser()
    }

    override fun onGetViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentCartBinding = FragmentCartBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        cartProductAdapter = CartProductAdapter(object : CartProductAdapterListener {
            override fun onAdapterItemClicked(position: Int, item: ProductItem, view: View?) {
                findNavController().navigate(R.id.action_cart_to_details, bundleOf(DetailsFragment.KEY_PRODUCT_ITEM to item, KEY_SHOW_BUTTON to false))
            }

            override fun updateProductToCart(productItem: ProductItem) {
                QuantityInputDialog(requireContext(), productItem.quantity).show { quantity ->
                    if(quantity > 0) {
                        productItem.quantity = quantity
                        viewModel.updateQuantityProductItem(productItem.toProductItemModel())
                    } else {
                        viewModel.deleteProductItem(productItem.toProductItemModel())
                    }
                }
            }

            override fun deleteProductToCart(productItem: ProductItem) {
                showConfirmDeletionProduct(productItem)
            }
        })

        with(binding) {
            recyclerCartProductItemsView.adapter = cartProductAdapter
            notProductsCartView.retryButton.setOnClickListener {
                navToHome()
            }
            confirmOrderView.confirmOrderView.setOnClickListener {
                showConfirmationDialog(binding.confirmOrderView.totalValueView.text.toString(), cartProductAdapter.getProductItems().size)
            }

            errorView.retryButton.setOnClickListener {
                viewModel.getProductsItems()
            }

            successOrderView.navToOrderView.setOnClickListener {
                navToOrderHistory()
            }
        }
    }

    override fun onInitViewModel() {
        with(viewModel) {
            userLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Success -> {
                        it.data?.let {
                            viewModel.getProductsItems()
                        } ?: run {
                            findNavController().navigate(R.id.action_cart_to_login)
                        }
                    }

                    is ResponseState.Error -> {
                        findNavController().navigate(R.id.action_cart_to_login)
                    }

                    else -> {}
                }
            }

            productsItemsLive.observe(viewLifecycleOwner) {
                when(it) {
                    is ResponseState.Loading -> {
                        if(cartProductAdapter.getProductItems().isEmpty()) {
                            if(it.isLoading) {
                                showLoading()
                            } else {
                                hideLoading()
                            }
                        }
                    }
                    is ResponseState.Success -> {
                        val items = it.data.productItems.map { it.toProductItem() }
                        cartProductAdapter.submitList(items)
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
                            cartProductAdapter.updateItem(it.data.toProductItem())
                            requireView().showSnackbar(getString(R.string.updated_quantity_product))
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.updated_quantity_product_error)) {
                                it.action?.invoke()
                            }
                        }

                        else -> {}
                    }
                }
            }

            deleteProductToCartLive.observe(viewLifecycleOwner) {
                it?.let {
                    when(it) {
                        is ResponseState.Success -> {
                            cartProductAdapter.removeItem(it.data.toProductItem())
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

                        else -> {}
                    }
                }
            }

            createOrderLive.observe(viewLifecycleOwner) {
                it?.let {
                    when(it) {
                        is ResponseState.Success -> {
                            cartProductAdapter.submitList(listOf())
                            showViewSuccessOrder()
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.create_order_error)) {
                                it.action?.invoke()
                            }
                        }

                        else -> {}
                    }
                }
            }

            totalPrice.observe(viewLifecycleOwner) {
                binding.confirmOrderView.totalValueView.text = it.toCurrency()
            }

            totalQuantity.observe(viewLifecycleOwner) {
                binding.confirmOrderView.quantityValueView.text = it.toString()
            }

        }
    }

    private fun showLoading() {
        with(binding) {
            recyclerCartProductItemsView.gone()
            confirmOrderView.root.gone()
            errorView.root.gone()
            notProductsCartView.root.gone()
            loadingView.visible()
        }
    }

    private fun hideLoading() {
        binding.loadingView.gone()
    }

    private fun showView() {
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

    private fun showViewSuccessOrder() {
        with(binding) {
            recyclerCartProductItemsView.gone()
            confirmOrderView.root.gone()
            errorView.root.gone()
            notProductsCartView.root.gone()
            successOrderView.root.visible()
        }
    }

    private fun showErrorView() {
        with(binding) {
            confirmOrderView.root.gone()
            notProductsCartView.root.gone()
            recyclerCartProductItemsView.gone()
            errorView.root.visible()
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
        val message = getString(R.string.confirm_order_message, totalValue, quantityItems.toString())
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
        requireActivity().findViewById<ImageView>(R.id.clearCartView).apply {
            setOnClickListener {
                showConfirmClearCart()
            }

            if (cartProductAdapter.getProductItems().isNotEmpty() && show) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun navToHome() {
        requireActivity().supportFragmentManager.popBackStackImmediate()
    }

    private fun navToOrderHistory() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, inclusive = false, saveState = true)
            .setRestoreState(true)
            .build()
        findNavController().navigate(R.id.nav_order_history, null, navOptions)
    }
}