package com.example.studentmarketplaceapp.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmarketplaceapp.viewModel.PaymentViewModel
import com.example.studentmarketplaceapp.viewModel.PostItemViewModel

class PostItemFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostItemViewModel::class.java)) {
            return PostItemViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class PostItemVm")
    }
}