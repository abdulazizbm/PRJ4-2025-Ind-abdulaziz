    package com.example.studentmarketplaceapp.model

    import com.google.gson.annotations.SerializedName
    import java.time.LocalDate

    data class Message(
        @SerializedName("_id")
        val id: String,
        val content: String,
        val sent_at: String ,
        val sender_id: String,
        val receiver_id: String,
        val product_id: String
    )