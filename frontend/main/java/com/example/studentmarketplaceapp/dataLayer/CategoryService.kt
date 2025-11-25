package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.Category
import retrofit2.http.GET
import retrofit2.http.Path

interface CategoryService {
    @GET("categories")
    suspend fun getAllCategories(): List<Category>

    @GET("categories/{id}")
    suspend fun findById(@Path("id") id: String): Category




}