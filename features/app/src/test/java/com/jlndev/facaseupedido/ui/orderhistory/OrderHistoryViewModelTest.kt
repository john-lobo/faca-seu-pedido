package com.jlndev.facaseupedido.ui.orderhistory

import androidx.lifecycle.Observer
import com.jlndev.baseservice.state.ViewState
import com.jlndev.facaseupedido.BaseViewModelTest
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.OrderHistoryItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.util.concurrent.TimeUnit

class OrderHistoryViewModelTest: BaseViewModelTest() {

    private lateinit var viewModel: OrderHistoryViewModel
    private lateinit var orderHistoryRepository: OrderHistoryRepository
    private lateinit var onGetOrdersHistoryObserver: Observer<ViewState<List<OrderHistoryItemModel>>>

    private val orderHistoryItemModel = OrderHistoryItemModel(1, listOf(),1, 100.0)

    override fun setup() {
        orderHistoryRepository = mockk()
        onGetOrdersHistoryObserver = mockk(relaxed = true)
        viewModel = instantiateViewModel()
    }

    @Test
    fun test_getAllOrders_success() {
        // Arrange
        every { orderHistoryRepository.getAllOrders() } returns Single.just(listOf(orderHistoryItemModel))

        // Act
        viewModel.getAllOrders()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { orderHistoryRepository.getAllOrders() }
        verify { onGetOrdersHistoryObserver.onChanged(ViewState.Success(listOf(orderHistoryItemModel))) }
    }

    @Test
    fun test_getAllOrders_error() {
        // Arrange
        val error = Throwable("Erro na busca dos pedidos")
        every { orderHistoryRepository.getAllOrders() } returns Single.error(error)


        // Act
        viewModel.getAllOrders()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { orderHistoryRepository.getAllOrders() }
        verify { onGetOrdersHistoryObserver.onChanged(ViewState.Error(error)) }
    }

    private fun instantiateViewModel(): OrderHistoryViewModel {
        val viewModel = OrderHistoryViewModel(testScheduler, orderHistoryRepository)
        observeLiveData(viewModel.getOrdersHistoryLive, onGetOrdersHistoryObserver)
        return viewModel
    }
}