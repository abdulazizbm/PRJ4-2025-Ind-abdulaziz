package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Product(
    @SerializedName("_id")
    val id: String,

    @SerializedName("seller_id")
    val sellerId: String,

    val title: String,
    val description: String,
    val price: Double,
    val status: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("category_id")
    val categoryId: String
)