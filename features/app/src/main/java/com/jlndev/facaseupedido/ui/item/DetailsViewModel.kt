package com.jlndev.facaseupedido.ui.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.productservice.data.remote.model.Cart
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel

class DetailsViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val cartRepository: CartRepository
) : BaseViewModel() {

    private val _addProductToCartLive = MutableLiveData<ViewState<Cart>?>()
    val addProductToCartLive : LiveData<ViewState<Cart>?>
        get() = _addProductToCartLive

    fun addProductToCart(itemModel: ProductItemModel) {
        cartRepository.insertProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _addProductToCartLive.value = ViewState.Success(it)
                _addProductToCartLive.value = null
            }
            .doOnError {
                val callback = { addProductToCart(itemModel) }
                _addProductToCartLive.value = ViewState.Error(it, callback)
                _addProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }
}