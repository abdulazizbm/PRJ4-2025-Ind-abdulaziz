package com.example.studentmarketplaceapp.ui.screen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.viewModel.PaymentViewModel
import com.example.studentmarketplaceapp.viewModel.UserViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import com.example.studentmarketplaceapp.Factory.PaymentViewModelFactory

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentScreen(
    viewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current)),
    productId: String,
    sellerId: String,
    amount: String,
    onSuccess: () -> Unit,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current))
) {

    val context = LocalContext.current
    val stripeUrl by viewModel.stripeCheckoutUrl.collectAsState()
    val paymentStatus by viewModel.paymentStatus.collectAsState()
    val user by userViewModel.userFlow.collectAsState()
    val buyerId = user?.id

    // Trigger Stripe session creation once buyer and seller IDs are available
    LaunchedEffect(buyerId, sellerId) {
        if (buyerId != null) {
            viewModel.createStripeCheckoutSession(
                amount = amount.toDouble(),
                sellerId = sellerId,
                productId = productId
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Paying: $amount USD")

        if (stripeUrl != null && buyerId != null) {
            AndroidView(factory = {
                WebView(it).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            url?.let {
                                when {
                                    url.contains("stripe-success") -> {
                                        Log.d("PaymentScreen", "Stripe payment success")
                                        viewModel.markPaymentSuccessful()
                                    }
                                    url.contains("stripe-cancel") -> {
                                        Log.d("PaymentScreen", "Stripe payment cancelled")
                                        viewModel.markPaymentCancelled()
                                    }
                                    else -> {
                                        Log.d("PaymentScreen", "Navigated to: $url")
                                    }
                                }
                            }
                        }
                    }
                    loadUrl(stripeUrl!!)
                }
            })
        } else if (buyerId == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("User not logged in. Please log in to proceed.", color = MaterialTheme.colorScheme.error)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        paymentStatus.takeIf { it.isNotEmpty() }?.let {
            Text(text = it)
            if (it.contains("successful", ignoreCase = true)) {
                Button(onClick = onSuccess) {
                    Text("Continue")
                }
            }
        }
    }
}