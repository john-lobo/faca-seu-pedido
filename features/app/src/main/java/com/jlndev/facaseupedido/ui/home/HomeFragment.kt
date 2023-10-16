package com.jlndev.facaseupedido.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentHomeBinding
import com.jlndev.facaseupedido.ui.components.QuantityInputDialog
import com.jlndev.facaseupedido.ui.home.adapter.ProductAdapter
import com.jlndev.facaseupedido.ui.home.adapter.ext.toProductItem
import com.jlndev.facaseupedido.ui.home.adapter.ext.toProductItemModel
import com.jlndev.facaseupedido.ui.home.adapter.model.ProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter

    companion object {
        const val KEY_PRODUCT_ITEM = "KEY_PRODUCT_ITEM"
    }

    override fun onInitData() {
        viewModel.getProductsItems()
    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        productAdapter = ProductAdapter(object : ProductAdapter.ProductAdapterListener {
            override fun onAdapterItemClicked(position: Int, item: ProductItem, view: View?) {
            }

            override fun addProcutToCart(productItem: ProductItem) {
                QuantityInputDialog(requireContext()).show { quantity ->
                    productItem.quantity = quantity
                    viewModel.addProductToCart(productItem.toProductItemModel())
                }
            }
        })

        binding.recyclerProductItemsView.adapter = productAdapter
    }

    override fun onInitViewModel() {
        with(viewModel) {
            productsItemsLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Success -> {
                        val productItems = it.data.map { it.toProductItem() }
                        productAdapter.submitList(productItems)
                        showView()
                    }

                    is ResponseState.Error -> {
                        showErrorView()
                    }
                }
            }

            productsItemsLoadingLive.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }

            addProductToCartLive.observe(viewLifecycleOwner) {
                it?.let { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            requireView().showSnackbar(getString(R.string.product_added_to_cart), getString(R.string.go_to_cart)) {
                                navToCart()
                            }
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.error_add_to_cart), getString(R.string.try_again)) {
                                state.action?.invoke()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun showLoading() {
        binding.loadingView.visible()
        binding.recyclerProductItemsView.gone()
        binding.errorView.root.gone()
    }

    override fun hideLoading() {
        binding.loadingView.gone()
    }

    override fun showView() {
        binding.errorView.root.gone()
        binding.recyclerProductItemsView.visible()
    }

    override fun showErrorView() {
        binding.recyclerProductItemsView.gone()
        binding.errorView.root.visible()
    }

    private fun navToCart() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, inclusive = false, saveState = true)
            .setRestoreState(true)
            .build()
        findNavController().navigate(R.id.nav_cart, null, navOptions)
    }
}