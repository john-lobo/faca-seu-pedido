package com.jlndev.facaseupedido.ui.item

import androidx.lifecycle.Observer
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.facaseupedido.BaseViewModelTest
import com.jlndev.productservice.data.repository.CartRepository
import com.jlndev.productservice.data.repository.model.ProductItemModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import org.junit.Test
import java.util.concurrent.TimeUnit

class DetailsViewModelTest : BaseViewModelTest() {

    private lateinit var onAddProductToCartObserver: Observer<ResponseState<ProductItemModel>?>
    private lateinit var cartRepository: CartRepository
    private lateinit var viewModel: DetailsViewModel

    private val productItemModel = ProductItemModel(1, "titulo","descrição", 100.0, "imagem", 1)

    override fun setup() {
        cartRepository = mockk()
        onAddProductToCartObserver = mockk(relaxed = true)
        viewModel = instantiateViewModel()
    }

    @Test
    fun test_addProductToCart_success() {
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
    fun test_addProductToCart_error() {
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

    private fun instantiateViewModel(): DetailsViewModel {
        val viewModel = DetailsViewModel(testScheduler, cartRepository)
        observeLiveData(viewModel.addProductToCartLive, onAddProductToCartObserver)
        return viewModel
    }
}