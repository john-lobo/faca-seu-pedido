package com.jlndev.facaseupedido.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseFragment
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentHomeBinding
import com.jlndev.facaseupedido.ui.home.adapter.ProductAdapter
import com.jlndev.facaseupedido.ui.home.adapter.ProductAdapterListener
import com.jlndev.facaseupedido.ui.item.DetailsFragment.Companion.KEY_PRODUCT_ITEM
import com.jlndev.facaseupedido.ui.uitls.components.QuantityInputDialog
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItem
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItemModel
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import com.jlndev.firebaseservice.data.user.UserSingleton
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private val firebaseAuth: FirebaseAuth by inject()
    private val fireStore: FirebaseFirestore by inject()

    override val viewModel: HomeViewModel by viewModel()
    private lateinit var productAdapter: ProductAdapter

    override fun onInitData() {
        UserSingleton.loadUserFromFirebase(fireStore, firebaseAuth)
        viewModel.getProductsItems()
    }

    override fun onGetViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        productAdapter = ProductAdapter(object : ProductAdapterListener {
            override fun onAdapterItemClicked(position: Int, item: ProductItem, view: View?) {
                findNavController().navigate(R.id.action_home_to_details, bundleOf(KEY_PRODUCT_ITEM to item))
            }

            override fun addProductToCart(productItem: ProductItem) {
                firebaseAuth.currentUser?.let {
                    QuantityInputDialog(requireContext()).show { quantity ->
                        if(quantity > 0) {
                            productItem.quantity = quantity
                            viewModel.addProductToCart(productItem.toProductItemModel())
                        }
                    }
                } ?: run {
                    findNavController().navigate(R.id.action_home_to_login)
                }
            }
        })

        binding.recyclerProductItemsView.adapter = productAdapter

        UserSingleton.getUser().observe(viewLifecycleOwner) {
            with(binding.nomeLoginView) {
                it?.let {
                    text = getString(R.string.welcome_user, it.name.split(" ")[0])
                } ?: run {
                    text = getString(R.string.login_or_register)
                    setOnClickListener {
                        findNavController().navigate(R.id.action_home_to_login)
                    }
                }
            }
        }
    }

    override fun onInitViewModel() {
        with(viewModel) {
            productsItemsLive.observe(viewLifecycleOwner) {
                when (it) {
                    is ResponseState.Loading -> {
                        if (it.isLoading) {
                            showLoading()
                        } else {
                            hideLoading()
                        }
                    }
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

            addProductToCartLive.observe(viewLifecycleOwner) {
                it?.let { state ->
                    when (state) {
                        is ResponseState.Success -> {
                            requireView().showSnackbar(getString(R.string.product_added_to_cart), getString(R.string.go_to_cart)) {
                                navToCart()
                            }
                        }
                        is ResponseState.Error -> {
                            requireView().showSnackbar(getString(R.string.error_add_to_cart), getString(
                                com.jlndev.coreandroid.R.string.try_again)) {
                                state.action?.invoke()
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    override fun showLoading() {
        with(binding) {
            loadingView.visible()
            recyclerProductItemsView.gone()
            errorView.root.gone()
        }
    }

    override fun hideLoading() {
        binding.loadingView.gone()
    }

    override fun showView() {
        with(binding) {
            errorView.root.gone()
            recyclerProductItemsView.visible()
        }
    }

    override fun showErrorView() {
        with(binding) {
            recyclerProductItemsView.gone()
            errorView.root.visible()
            errorView.retryButton.setOnClickListener {
                viewModel.getProductsItems()
            }
        }
    }

    private fun navToCart() {
        val navOptions: NavOptions = NavOptions.Builder()
            .setPopUpTo(R.id.nav_home, inclusive = false, saveState = true)
            .setRestoreState(true)
            .build()
        findNavController().navigate(R.id.nav_cart, null, navOptions)
    }
}