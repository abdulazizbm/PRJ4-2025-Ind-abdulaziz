package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.RefreshTokenRequest
import kotlinx.coroutines.launch

class LogoutVm(private val context: Context): ViewModel() {

    private val api = RetrofitInstance.userService
    private val tokenDataStore = TokenDataStore(context)


    private val _logoutState = MutableLiveData<Boolean>()
    val logoutState: LiveData<Boolean> = _logoutState

    fun logout() {
        viewModelScope.launch {
            val token = tokenDataStore.getToken()
            val refreshToken = tokenDataStore.getRefreshToken()

            if (token != null && refreshToken != null) {
                try {
                    val response = api.logout("Bearer $token", RefreshTokenRequest(refreshToken))
                    if (response.isSuccessful) {
                        tokenDataStore.clearToken()
                        _logoutState.value = true
                    } else {
                        _logoutState.value = false
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _logoutState.value = false
                }
            } else {
                _logoutState.value = false
            }
        }
    }
}