package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.util.Log
import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.StripeCheckoutRequest
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.dataLayer.TransactionService
import com.example.studentmarketplaceapp.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.time.LocalDate

class PaymentViewModel(private val context: Context) : ViewModel() {

    private val api: TransactionService = RetrofitInstance.transactionService
    private val tokenDataStore = TokenDataStore(context)

    private val _stripeCheckoutUrl = MutableStateFlow<String?>(null)
    val stripeCheckoutUrl: StateFlow<String?> = _stripeCheckoutUrl

    private val _paymentStatus = MutableStateFlow("")
    val paymentStatus: StateFlow<String> = _paymentStatus

    fun createStripeCheckoutSession(amount: Double, sellerId: String, productId: String) {
        viewModelScope.launch {
            _paymentStatus.value = "Creating Stripe session..."
            try {
                val token = tokenDataStore.getToken()
                val response = api.createStripeCheckoutSession(
                    "Bearer $token",
                    StripeCheckoutRequest(amount, "usd", sellerId, productId)
                )
                if (response.isSuccessful) {
                    _stripeCheckoutUrl.value = response.body()?.url
                    _paymentStatus.value = "Stripe session ready"
                } else {
                    _paymentStatus.value = "Failed to create session"
                }
            } catch (e: Exception) {
                _paymentStatus.value = "Error: ${e.localizedMessage}"
            }
        }
    }

    fun markPaymentSuccessful() {
        _paymentStatus.value = "Payment successful!"
    }

    fun markPaymentCancelled() {
        _paymentStatus.value = "Payment was cancelled."
    }
}
