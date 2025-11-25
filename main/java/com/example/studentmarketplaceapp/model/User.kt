package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String?,
    val username: String,
    val email: String,
    val password: String,
)