package com.jlndev.facaseupedido.ui.home

import androidx.lifecycle.Observer
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.facaseupedido.BaseViewModelTest
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.ProductRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.util.concurrent.TimeUnit

class HomeViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var onProductsItemsObserver: Observer<ResponseState<List<ProductItemModel>>>
    private lateinit var onAddProductToCartObserver: Observer<ResponseState<ProductItemModel>?>

    private val productItemModel = ProductItemModel(1, "titulo","descrição", 100.0, "imagem", 1)

    override fun setup() {
        productRepository = mockk()
        cartRepository = mockk()
        onProductsItemsObserver = mockk(relaxed = true)
        onAddProductToCartObserver = mockk(relaxed = true)
        viewModel = instantiateViewModel()
    }

    @Test
    fun test_getProductsItems_success() {
        // Arrange
        every { productRepository.getProductsItems() } returns Single.just(listOf(productItemModel))

        // Act
        viewModel.getProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { productRepository.getProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Success(listOf(productItemModel))) }
    }

    @Test
    fun test_getProductsItems_error() {
        // Arrange
        val error = Throwable("Erro na busca dos itens")
        every { productRepository.getProductsItems() } returns Single.error(error)


        // Act
        viewModel.getProductsItems()
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { productRepository.getProductsItems() }
        verify { onProductsItemsObserver.onChanged(ResponseState.Error(error)) }
    }

    @Test
    fun test_updateQuantityProductItem_success() {
        // Arrange
        every { cartRepository.getProductItem(productItemModel) } returns Single.just(productItemModel)

        // Act
        viewModel.addProductToCart(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.getProductItem(productItemModel) }
        verify { onAddProductToCartObserver.onChanged(ResponseState.Success(productItemModel)) }
        verify { onAddProductToCartObserver.onChanged(null) }
    }

    @Test
    fun test_updateQuantityProductItem_error() {
        // Arrange
        val error = Throwable("Erro ao inserir item")
        every { cartRepository.getProductItem(productItemModel) } returns Single.error(error)

        // Act
        viewModel.addProductToCart(productItemModel)
        testScheduler.test().advanceTimeBy(1, TimeUnit.SECONDS)

        // Assert
        verify { cartRepository.getProductItem(productItemModel) }
        verify { onAddProductToCartObserver.onChanged(null) }
    }

    private fun instantiateViewModel(): HomeViewModel {
        val viewModel = HomeViewModel(testScheduler, productRepository, cartRepository)
        observeLiveData(viewModel.productsItemsLive, onProductsItemsObserver)
        observeLiveData(viewModel.addProductToCartLive, onAddProductToCartObserver)
        return viewModel
    }
}