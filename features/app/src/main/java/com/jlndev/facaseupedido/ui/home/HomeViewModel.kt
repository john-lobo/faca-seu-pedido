package com.jlndev.facaseupedido.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.ProductRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel

class HomeViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val orderHistoryRepository: OrderHistoryRepository
) : BaseViewModel() {

    private val _productsItemsLive = MutableLiveData<ResponseState<List<ProductItemModel>>>()
    val productsItemsLive : LiveData<ResponseState<List<ProductItemModel>>>
        get() = _productsItemsLive

    private val _addProductToCartLive = MutableLiveData<ResponseState<ProductItemModel>?>()
    val addProductToCartLive : LiveData<ResponseState<ProductItemModel>?>
        get() = _addProductToCartLive

    private val _productsItemsLoadingLive = MutableLiveData<Boolean>()
    val productsItemsLoadingLive: LiveData<Boolean>
        get() = _productsItemsLoadingLive

    fun getProductsItems () {
        productRepository.getProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _productsItemsLoadingLive.value = true }
            .doFinally { _productsItemsLoadingLive.value = false }
            .doOnSuccess {
                _productsItemsLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _productsItemsLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun addProductToCart(itemModel: ProductItemModel) {
        cartRepository.getProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _addProductToCartLive.value = ResponseState.Success(it)
                _addProductToCartLive.value = null
            }
            .doOnError {
                val callback = { addProductToCart(itemModel) }
                _addProductToCartLive.value = ResponseState.Error(it, callback)
                _addProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }
}