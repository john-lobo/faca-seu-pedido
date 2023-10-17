package com.jlndev.facaseupedido.ui.cart

import androidx.lifecycle.Observer
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.facaseupedido.BaseViewModelTest
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.OrderHistoryRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Test
import java.util.concurrent.TimeUnit

class CartViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: CartViewModel
    private lateinit var cartRepository: CartRepository
    private lateinit var orderHistoryRepository: OrderHistoryRepository
    private lateinit var onProductsItemsObserver: Observer<ResponseState<List<ProductItemModel>>>
    private lateinit var onProductToCartObserver: Observer<ResponseState<ProductItemModel>?>
    private lateinit var onCreateOrderLiveObserver: Observer<ResponseState<Unit>?>

    private val productItemModel = ProductItemModel(1, "titulo","descrição", 100.0, "imagem", 1)

    override fun setup() {
        cartRepository = mockk()
        orderHistoryRepository = mockk()
        onProductsItemsObserver = mockk(relaxed = true)
        onProductToCartObserver = mockk(relaxed = true)
        onCreateOrderLiveObserver = mockk(relaxed = true)
        viewModel = instantiateViewModel()
    }

    @Test
    fun test_getProductsItems_success() {
        // Arrange
        every { cartRepository.getProductsItems() } returns Single.just(listOf(productItemModel))

        // Act
        viewModel.getProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.getProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Success(listOf(productItemModel))) }
    }

    @Test
    fun test_getProductsItems_error() {
        // Arrange
        val error = Throwable("Erro na busca dos itens")
        every { cartRepository.getProductsItems() } returns Single.error(error)


        // Act
        viewModel.getProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.getProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Error(error)) }
    }

    @Test
    fun test_updateQuantityProductItem_success() {
        // Arrange
        every { cartRepository.insertProductItem(productItemModel) } returns Single.just(productItemModel)

        // Act
        viewModel.updateQuantityProductItem(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.insertProductItem(productItemModel) }
        verify { onProductToCartObserver.onChanged(ResponseState.Success(productItemModel)) }
        verify { onProductToCartObserver.onChanged(null) }
    }

    @Test
    fun test_updateQuantityProductItem_error() {
        // Arrange
        val error = Throwable("Erro ao inserir item")
        every { cartRepository.insertProductItem(productItemModel) } returns Single.error(error)

        // Act
        viewModel.updateQuantityProductItem(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.insertProductItem(productItemModel) }
        verify { onProductToCartObserver.onChanged(null) }
    }

    @Test
    fun test_deleteProductItem_success() {
        // Arrange
        every { cartRepository.deleteProductItem(productItemModel) } returns Single.just(productItemModel)

        // Act
        viewModel.deleteProductItem(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.deleteProductItem(productItemModel) }
        verify { onProductToCartObserver.onChanged(ResponseState.Success(productItemModel)) }
        verify { onProductToCartObserver.onChanged(null) }
    }

    @Test
    fun test_deleteProductItem_error() {
        // Arrange
        val error = Throwable("Erro ao deletar item")
        every { cartRepository.deleteProductItem(productItemModel) } returns Single.error(error)

        // Act
        viewModel.deleteProductItem(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.deleteProductItem(productItemModel) }
        verify { onProductToCartObserver.onChanged(null) }
    }

    @Test
    fun test_deleteAllProductsItems_success() {
        // Arrange
        every { cartRepository.deleteAllProductsItems() } returns Single.just(listOf())

        // Act
        viewModel.deleteAllProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.deleteAllProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Success(listOf())) }
    }

    @Test
    fun test_deleteAllProductsItems_error() {
        // Arrange
        val error = Throwable("Erro na ao excluir itens")
        every { cartRepository.deleteAllProductsItems() } returns Single.error(error)


        // Act
        viewModel.deleteAllProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.deleteAllProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Error(error)) }
    }

    @Test
    fun test_insertOrder_success() {
        // Arrange
        every { orderHistoryRepository.insertOrder(listOf(productItemModel)) } returns Completable.complete()

        // Act
        viewModel.insertOrder(listOf(productItemModel))
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { orderHistoryRepository.insertOrder(listOf(productItemModel)) }
        verify { onCreateOrderLiveObserver.onChanged(ResponseState.Success(Unit)) }
        verify { onCreateOrderLiveObserver.onChanged(null) }
        verify { onProductsItemsObserver.onChanged(ResponseState.Success(listOf())) }
    }

    @Test
    fun test_insertOrder_error() {
        // Arrange
        val error = Throwable("Erro ao realizar pedido")
        every { orderHistoryRepository.insertOrder(listOf(productItemModel)) } returns Completable.error(error)

        // Act
        viewModel.insertOrder(listOf(productItemModel))
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { orderHistoryRepository.insertOrder(listOf(productItemModel)) }
        verify { onCreateOrderLiveObserver.onChanged(null) }
    }

    private fun instantiateViewModel(): CartViewModel {
        val viewModel = CartViewModel(testScheduler, cartRepository, orderHistoryRepository)
        observeLiveData(viewModel.productsItemsLive, onProductsItemsObserver)
        observeLiveData(viewModel.updateQuantityProductToCartLive, onProductToCartObserver)
        observeLiveData(viewModel.deleteProductToCartLive, onProductToCartObserver)
        observeLiveData(viewModel.createOrderLive, onCreateOrderLiveObserver)
        return viewModel
    }

}
