package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.Transaction
import retrofit2.Response // Import Response for better error handling
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class StripeCheckoutRequest(
    val amount: Double,
    val currency: String,
    val seller_id: String,
    val productId: String
)

data class StripeCheckoutResponse(val url: String)

interface TransactionService {
    @POST("/stripe/create-checkout")
    suspend fun createStripeCheckoutSession(
        @Header("Authorization") token: String,
        @Body request: StripeCheckoutRequest
    ): Response<StripeCheckoutResponse>
}
