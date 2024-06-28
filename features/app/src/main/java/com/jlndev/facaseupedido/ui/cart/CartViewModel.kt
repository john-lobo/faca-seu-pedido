package com.jlndev.facaseupedido.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processCompletable
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
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

    private val _productsItemsLive = MutableLiveData<ViewState<Cart>>()
    val productsItemsLive : LiveData<ViewState<Cart>>
        get() = _productsItemsLive

    private val _updateQuantityProductToCartLive = MutableLiveData<ViewState<ProductItemModel>?>()
    val updateQuantityProductToCartLive : LiveData<ViewState<ProductItemModel>?>
        get() = _updateQuantityProductToCartLive

    private val _deleteProductToCartLive = MutableLiveData<ViewState<ProductItemModel>?>()
    val deleteProductToCartLive : LiveData<ViewState<ProductItemModel>?>
        get() = _deleteProductToCartLive

    private val _createOrderLive = MutableLiveData<ViewState<Unit>?>()
    val createOrderLive : LiveData<ViewState<Unit>?>
        get() = _createOrderLive

    var totalPrice = MutableLiveData<Double>()
    var totalQuantity = MutableLiveData<Long>()

    private val _userLive = MutableLiveData<ViewState<User?>>()
    val userLive : LiveData<ViewState<User?>>
        get() = _userLive

    fun getUser() {
        userRepository.getUser()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Success(it)
            }
            .doOnError {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun getProductsItems() {
        cartRepository.getProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe {  _productsItemsLive.value = ViewState.Loading(true) }
            .doOnSuccess {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Success(it)
                updateCartValue(it.totalPrice, it.totalQuantity)
            }
            .doOnError {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun updateQuantityProductItem(itemModel: ProductItemModel) {
        cartRepository.updateQuantityProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                val updatedItem = it.productItems.find { it.id == itemModel.id }!!
                _updateQuantityProductToCartLive.value = ViewState.Success(updatedItem)
                _updateQuantityProductToCartLive.value = null
                updateCartValue(it.totalPrice, it.totalQuantity)
            }
            .doOnError {
                val callback = { updateQuantityProductItem(itemModel) }
                _updateQuantityProductToCartLive.value = ViewState.Error(it, callback)
                _updateQuantityProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }

    fun deleteProductItem(itemModel: ProductItemModel) {
        cartRepository.deleteProductItem(itemModel)
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _deleteProductToCartLive.value = ViewState.Success(itemModel)
                _deleteProductToCartLive.value = null
                updateCartValue(it.totalPrice, it.totalQuantity)
            }
            .doOnError {
                val callback = { deleteProductItem(itemModel) }
                _deleteProductToCartLive.value = ViewState.Error(it, callback)
                _deleteProductToCartLive.value = null
            }
            .disposedBy(disposables)
    }

    fun deleteAllProductsItems() {
        cartRepository.deleteAllProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _productsItemsLive.value = ViewState.Loading(true) }
            .doOnSuccess {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Success(it)
                updateCartValue(it.totalPrice, it.totalQuantity)
            }
            .doOnError {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun insertOrder(productsItems: List<ProductItemModel>) {
        orderHistoryRepository.insertOrder(productsItems)
            .processCompletable(schedulerProvider)
            .doOnComplete {
                _createOrderLive.value = ViewState.Success(Unit)
                _createOrderLive.value = null
                deleteAllProductsItems()
            }
            .doOnError {
                val callback = { insertOrder(productsItems) }
                _createOrderLive.value = ViewState.Error(it, callback)
                _createOrderLive.value = null
            }
            .disposedBy(disposables)
    }

     private fun updateCartValue(totalPrice: Double, totalQuantity: Long) {
        this.totalPrice.value = totalPrice
        this.totalQuantity.value = totalQuantity
    }
}