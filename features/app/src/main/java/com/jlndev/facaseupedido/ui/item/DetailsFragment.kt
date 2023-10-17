package com.jlndev.facaseupedido.ui.item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.fragment.BaseBottomSheetFragment
import com.jlndev.coreandroid.ext.getTypedParcelable
import com.jlndev.coreandroid.ext.gone
import com.jlndev.coreandroid.ext.loadImage
import com.jlndev.coreandroid.ext.showSnackbar
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.coreandroid.ext.visible
import com.jlndev.facaseupedido.R
import com.jlndev.facaseupedido.databinding.FragmentDetailsBinding
import com.jlndev.facaseupedido.ui.uitls.components.QuantityInputDialog
import com.jlndev.facaseupedido.ui.uitls.ext.toProductItemModel
import com.jlndev.facaseupedido.ui.uitls.model.ProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment: BaseBottomSheetFragment<FragmentDetailsBinding, DetailsViewModel>() {

    private lateinit var item: ProductItem

    override val viewModel: DetailsViewModel by viewModel()
    private var showButtonCart = true

    companion object {
        const val KEY_PRODUCT_ITEM = "KEY_PRODUCT_ITEM"
        const val KEY_SHOW_BUTTON = "KEY_SHOW_BUTTON"
    }

    override fun onInitData() {
        arguments?.getTypedParcelable(KEY_PRODUCT_ITEM, ProductItem::class.java)?.let {
            item = it
        }

        arguments?.getBoolean(KEY_SHOW_BUTTON, true)?.let {
            showButtonCart = it
        }
    }

    override fun onGetViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentDetailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)

    override fun onInitViews() {
        with(binding) {
            itemTitleView.text = item.title
            itemDescriptionView.text = item.description
            itemPriceView.text = item.price.toCurrency()
            itemImageview.loadImage(item.image)
            if(showButtonCart) {
                addCartView.visible()
                itemQuantityView.gone()
                addCartView.setOnClickListener {
                    QuantityInputDialog(requireContext()).show { quantity ->
                        item.quantity = quantity
                        viewModel.addProductToCart(item.toProductItemModel())
                    }
                }
            } else {
                itemQuantityView.visible()
                itemQuantityView.text = root.context.getString(R.string.quantity_label, item.quantity)
                addCartView.gone()
            }

        }
    }

    override fun onInitViewModel() {
        viewModel.addProductToCartLive.observe(viewLifecycleOwner) {
            it?.let { state ->
                when (state) {
                    is ResponseState.Success -> {
                        dismiss()
                        requireActivity().showSnackbar(getString(R.string.product_added_to_cart))
                    }
                    is ResponseState.Error -> {
                        dismiss()
                        requireActivity().showSnackbar(getString(R.string.error_add_to_cart))
                    }
                }
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

    override fun showLoading() {
//        TODO("Not yet implemented")
    }

    override fun hideLoading() {
//        TODO("Not yet implemented")
    }

    override fun showView() {
//        TODO("Not yet implemented")
    }

    override fun showErrorView() {
//        TODO("Not yet implemented")
    }
}