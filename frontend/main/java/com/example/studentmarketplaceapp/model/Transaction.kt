package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Transaction(
    @SerializedName("_id")
    val id: String,
    @SerializedName("buyer_id")
    val buyerId: String,
    @SerializedName("seller_id")
    val sellerId: String,
    @SerializedName("product_id")
    val productId: String,
    val transactionDate: LocalDate
)