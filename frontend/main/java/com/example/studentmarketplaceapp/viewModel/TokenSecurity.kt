package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.RefreshTokenRequest

class TokenSecurity(context: Context): ViewModel() {
    private val tokenDataStore = TokenDataStore(context)
    private val api = RetrofitInstance.userService

    suspend fun checkTokenAndRefresh(): String? {
        var token = tokenDataStore.getToken()

        if (token == null || tokenDataStore.isTokenExpired()) {
            Log.d("TokenSecurity", "Token is expired or not available, refreshing token...")

            val refreshToken = tokenDataStore.getRefreshToken()
            if (refreshToken != null) {
                try {
                    val response = api.refreshToken(RefreshTokenRequest(refreshToken))
                    val expirationTime = System.currentTimeMillis() + UserViewModel.TOKEN_EXPIRATION_DURATION

                    //save them tokens
                    tokenDataStore.saveToken(response.token, expirationTime)
                    tokenDataStore.saveRefreshToken(response.refreshToken)

                    Log.d("TokenSecurity", "New token saved: ${response.token}, with expiration time $expirationTime")

                    token = response.token
                    Log.d("TokenSecurity", "Token refreshed successfully")
                } catch (e: Exception) {
                    Log.e("TokenSecurity", "Error refreshing token: ${e.message}")
                    return null
                }
            } else {
                Log.d("TokenSecurity", "No refresh token available")
                return null
            }
        }

        return token
    }
}
