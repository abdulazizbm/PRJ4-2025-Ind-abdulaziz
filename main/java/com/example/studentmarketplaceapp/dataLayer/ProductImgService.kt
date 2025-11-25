package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.ProductImg
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class ProductImgInfo(
    val product_id: String,
    val image: String
)

interface ProductImgService {

    @POST("/product-imgs")
    suspend fun createProductImg(@Body info: ProductImgInfo): Response<ProductImg>

    @GET("/product-imgs/by-product/{productId}")
    suspend fun findByProductId(@Path("productId") productId: String): ProductImg
}