package com.jlndev.facaseupedido.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processCompletable
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.CartTotalQuantity
import com.jlndev.productservice.data.repository.model.ProductItemModel

class CartViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val cartRepository: CartRepository,
    private val orderHistoryRepository: OrderHistoryRepository
) : BaseViewModel() {

    private val _productsItemsLive = MutableLiveData<ResponseState<List<ProductItemModel>>>()
    val productsItemsLive : LiveData<ResponseState<List<ProductItemModel>>>
        get() = _productsItemsLive

    private val _updateQuantityProductToCartLive = MutableLiveData<ResponseState<ProductItemModel>?>()
    val updateQuantityProductToCartLive : LiveData<ResponseState<ProductItemModel>?>
        get() = _updateQuantityProductToCartLive

    private val _deleteProductToCartLive = MutableLiveData<ResponseState<ProductItemModel>?>()
    val deleteProductToCartLive : LiveData<ResponseState<ProductItemModel>?>
        get() = _deleteProductToCartLive

    private val _createOrderLive = MutableLiveData<ResponseState<Unit>?>()
    val createOrderLive : LiveData<ResponseState<Unit>?>
        get() = _createOrderLive

    private val _totalQuantityLive = MutableLiveData<ResponseState<CartTotalQuantity>>()
    val totalQuantityLive : LiveData<ResponseState<CartTotalQuantity>>
        get() = _totalQuantityLive

    fun getProductsItems() {
        cartRepository.getProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe {  _productsItemsLive.value = ResponseState.Loading(true) }
            .doOnSuccess {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Success(it)
                updateTotalAfterOperation()
            }
            .doOnError {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun updateQuantityProductItem(itemModel: ProductItemModel) {
        cartRepository.insertProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _updateQuantityProductToCartLive.value = ResponseState.Success(it)
                _updateQuantityProductToCartLive.value = null
                updateTotalAfterOperation()
            }
            .doOnError {
                val callback = { updateQuantityProductItem(itemModel) }
                _updateQuantityProductToCartLive.value = ResponseState.Error(it, callback)
                _updateQuantityProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }

    fun deleteProductItem(itemModel: ProductItemModel) {
        cartRepository.deleteProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _deleteProductToCartLive.value = ResponseState.Success(it)
                _deleteProductToCartLive.value = null
                updateTotalAfterOperation()
            }
            .doOnError {
                val callback = { deleteProductItem(itemModel) }
                _deleteProductToCartLive.value = ResponseState.Error(it, callback)
                _deleteProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }

    fun deleteAllProductsItems() {
        cartRepository.deleteAllProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _productsItemsLive.value = ResponseState.Loading(true) }
            .doOnSuccess {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Success(it)
                updateTotalAfterOperation()
            }
            .doOnError {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun insertOrder(productsItems: List<ProductItemModel>) {
        orderHistoryRepository.insertOrder(productsItems)
            .processCompletable(schedulerProvider)
            .doOnComplete {
                _createOrderLive.value = ResponseState.Success(Unit)
                _createOrderLive.value = null
                deleteAllProductsItems()
                updateTotalAfterOperation()
            }
            .doOnError {
                val callback = { insertOrder(productsItems) }
                _createOrderLive.value = ResponseState.Error(it, callback)
                _createOrderLive.value = null
            }
            .disposedBy(disposables)
    }

    private fun updateTotalAfterOperation() {
        cartRepository.updateTotalAfterOperation()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _totalQuantityLive.value = ResponseState.Loading(true) }
            .doOnSuccess {
                _totalQuantityLive.value = ResponseState.Loading(false)
                _totalQuantityLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _totalQuantityLive.value = ResponseState.Loading(false)
                _totalQuantityLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }
}