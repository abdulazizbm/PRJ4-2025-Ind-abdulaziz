package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.Product
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ProductInfo(
    val title: String,
    val price: Double,
    val description: String,
    @SerializedName("category_id")
    val categoryId: String,
)

interface ProductService {

    @GET("/products/{id}")
    suspend fun getProducts(@Path("id") id : String): Product

    @POST("/products")
    suspend fun createProduct(@Header("Authorization") token: String, @Body productInfo: ProductInfo): Response<Product>

    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @GET("/product/search")
    suspend fun getSearchedProducts(@Query("search") search: String) : List<Product>

    @GET("/debug/user")
    suspend fun debugUser(@Header("Authorization") token: String): Response<Map<String, String>>
}