package com.jlndev.facaseupedido.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.firebaseservice.data.user.UserRepository
import com.jlndev.firebaseservice.model.User
import com.jlndev.productservice.data.remote.model.Cart
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.ProductRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel

class HomeViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _productsItemsLive = MutableLiveData<ViewState<List<ProductItemModel>>>()
    val productsItemsLive : LiveData<ViewState<List<ProductItemModel>>>
        get() = _productsItemsLive

    private val _addProductToCartLive = MutableLiveData<ViewState<Cart>?>()
    val addProductToCartLive : LiveData<ViewState<Cart>?>
        get() = _addProductToCartLive

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
        productRepository.getProductsItems()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _productsItemsLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Success(it)
            }
            .doOnError {
                _productsItemsLive.value = ViewState.Loading(false)
                _productsItemsLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

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