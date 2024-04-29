package com.jlndev.facaseupedido.ui.orderhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.firebaseservice.data.user.UserRepository
import com.jlndev.firebaseservice.model.User
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel

class OrderHistoryViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val orderHistoryRepository: OrderHistoryRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _getOrdersHistoryLive = MutableLiveData<ResponseState<List<OrderHistoryItemModel>>>()
    val getOrdersHistoryLive : LiveData<ResponseState<List<OrderHistoryItemModel>>>
        get() = _getOrdersHistoryLive

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

    fun getAllOrders () {
        orderHistoryRepository.getAllOrders()
            .processSingle(schedulerProvider)
            .doOnSuccess {
                _getOrdersHistoryLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _getOrdersHistoryLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }
}