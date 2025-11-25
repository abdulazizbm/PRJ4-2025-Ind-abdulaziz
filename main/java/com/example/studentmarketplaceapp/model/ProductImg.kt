package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName

data class ProductImg(
    @SerializedName("_id")
    val id: String,
    val image: String,
    val product_id: String
)