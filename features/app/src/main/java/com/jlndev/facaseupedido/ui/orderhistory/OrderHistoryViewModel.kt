package com.jlndev.facaseupedido.ui.orderhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel

class OrderHistoryViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val orderHistoryRepository: OrderHistoryRepository
) : BaseViewModel() {

    private val _getOrdersHistoryLive = MutableLiveData<ResponseState<List<OrderHistoryItemModel>>>()
    val getOrdersHistoryLive : LiveData<ResponseState<List<OrderHistoryItemModel>>>
        get() = _getOrdersHistoryLive

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