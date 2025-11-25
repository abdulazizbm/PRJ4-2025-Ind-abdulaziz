package com.example.studentmarketplaceapp.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmarketplaceapp.viewModel.PaymentViewModel
import com.example.studentmarketplaceapp.viewModel.UserViewModel

class PaymentViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PaymentViewModel::class.java)) {
            return PaymentViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class payVm")
    }
}