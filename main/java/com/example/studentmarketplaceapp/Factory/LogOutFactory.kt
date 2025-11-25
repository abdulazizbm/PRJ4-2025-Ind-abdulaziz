package com.example.studentmarketplaceapp.Factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.studentmarketplaceapp.viewModel.LogoutVm
import com.example.studentmarketplaceapp.viewModel.UserViewModel

class LogOutFactory(private val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogoutVm::class.java)) {
            return LogoutVm(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class LogoutVm")
    }
}