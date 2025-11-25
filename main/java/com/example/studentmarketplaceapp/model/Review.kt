package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class Review(
    @SerializedName("_id")
    val id: String,
    val rating: Int,
    val comment: String,
    val createdAt: LocalDate,
    val reviewerId: String,
    val revieweeId: String
)