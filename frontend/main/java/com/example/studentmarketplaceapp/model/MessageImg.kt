package com.example.studentmarketplaceapp.model

import com.google.gson.annotations.SerializedName

data class MessageImg(
    @SerializedName("_id")
    val id: String? = null,
    val image: String,
    val message_id: String
)