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
import com.jlndev.productservice.data.repository.model.ProductItemModel

class CartViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val cartRepository: CartRepository,
    private val orderHistoryRepository: OrderHistoryRepository
) : BaseViewModel() {

    private val _productsItemsLive = MutableLiveData<ResponseState<List<ProductItemModel>>>()
    val productsItemsLive : LiveData<ResponseState<List<ProductItemModel>>>
        get() = _productsItemsLive

    private val _productsItemsLoadingLive = MutableLiveData<Boolean>()
    val productsItemsLoadingLive: LiveData<Boolean>
        get() = _productsItemsLoadingLive

    private val _updateQuantityProductToCartLive = MutableLiveData<ResponseState<ProductItemModel>?>()
    val updateQuantityProductToCartLive : LiveData<ResponseState<ProductItemModel>?>
        get() = _updateQuantityProductToCartLive

    private val _deleteProductToCartLive = MutableLiveData<ResponseState<ProductItemModel>?>()
    val deleteProductToCartLive : LiveData<ResponseState<ProductItemModel>?>
        get() = _deleteProductToCartLive

    private val _createOrderLive = MutableLiveData<ResponseState<Unit>?>()
    val createOrderLive : LiveData<ResponseState<Unit>?>
        get() = _createOrderLive

    fun getProductsItems() {
        cartRepository.getProductsItems()
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

    fun updateQuantityProductItem(itemModel: ProductItemModel) {
        cartRepository.insertProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _updateQuantityProductToCartLive.value = ResponseState.Success(it)
                _updateQuantityProductToCartLive.value = null
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

    fun insertOrder(productsItems: List<ProductItemModel>) {
        orderHistoryRepository.insertOrder(productsItems)
            .processCompletable(schedulerProvider)
            .doOnComplete {
                _createOrderLive.value = ResponseState.Success(Unit)
                _productsItemsLive.value = ResponseState.Success(listOf())
                _createOrderLive.value = null
            }
            .doOnError {
                val callback = { insertOrder(productsItems) }
                _createOrderLive.value = ResponseState.Error(it, callback)
                _createOrderLive.value = null
            }
            .disposedBy(disposables)
    }
}