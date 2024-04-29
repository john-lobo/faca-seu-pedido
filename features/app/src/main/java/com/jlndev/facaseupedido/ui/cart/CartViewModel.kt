package com.jlndev.facaseupedido.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processCompletable
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.coreandroid.ext.toCurrency
import com.jlndev.firebaseservice.data.user.UserRepository
import com.jlndev.firebaseservice.model.User
import com.jlndev.productservice.data.remote.model.Cart
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel

class CartViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val cartRepository: CartRepository,
    private val orderHistoryRepository: OrderHistoryRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _productsItemsLive = MutableLiveData<ResponseState<Cart>>()
    val productsItemsLive : LiveData<ResponseState<Cart>>
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

    var totalPrice = MutableLiveData<Double>()
    var totalQuantity = MutableLiveData<Long>()

    private val _userLive = MutableLiveData<ResponseState<User?>>()
    val userLive : LiveData<ResponseState<User?>>
        get() = _userLive

    fun getUser() {
        userRepository.getUser()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ResponseState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun getProductsItems() {
        cartRepository.getProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe {  _productsItemsLive.value = ResponseState.Loading(true) }
            .doOnSuccess {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Success(it)
                updateCartValue(it.totalPrice, it.totalQuantity)
            }
            .doOnError {
                _productsItemsLive.value = ResponseState.Loading(false)
                _productsItemsLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun updateQuantityProductItem(itemModel: ProductItemModel) {
        cartRepository.updateQuantityProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                val updatedItem = it.productItems.find { it.id == itemModel.id }!!
                _updateQuantityProductToCartLive.value = ResponseState.Success(updatedItem)
                _updateQuantityProductToCartLive.value = null
                updateCartValue(it.totalPrice, it.totalQuantity)
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
                val deleteItem = it.productItems.find { it.id == itemModel.id }!!
                _deleteProductToCartLive.value = ResponseState.Success(deleteItem)
                _deleteProductToCartLive.value = null
                updateCartValue(it.totalPrice, it.totalQuantity)
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
                updateCartValue(it.totalPrice, it.totalQuantity)
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
            }
            .doOnError {
                val callback = { insertOrder(productsItems) }
                _createOrderLive.value = ResponseState.Error(it, callback)
                _createOrderLive.value = null
            }
            .disposedBy(disposables)
    }

     private fun updateCartValue(totalPrice: Double, totalQuantity: Long) {
        this.totalPrice.value = totalPrice
        this.totalQuantity.value = totalQuantity
    }
}