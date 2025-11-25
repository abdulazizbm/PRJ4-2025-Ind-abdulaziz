package com.example.studentmarketplaceapp.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmarketplaceapp.viewModel.TokenSecurity

class TokenSecurityFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TokenSecurity::class.java)) {
            return TokenSecurity(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class TVM")
    }
}