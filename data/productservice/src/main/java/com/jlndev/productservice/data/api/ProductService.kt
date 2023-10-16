package com.jlndev.productservice.data.api

import com.jlndev.productservice.data.api.model.ProductItemResponse
import io.reactivex.Single
import retrofit2.http.GET

interface ProductService {
    @GET("products")
    fun getProductsItems(): Single<List<ProductItemResponse>>
}