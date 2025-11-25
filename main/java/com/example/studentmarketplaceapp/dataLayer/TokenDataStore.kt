package com.example.studentmarketplaceapp.dataLayer.TokenDataStore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import com.example.studentmarketplaceapp.dataLayer.dataStore.TokenPreferences
import com.example.studentmarketplaceapp.dataLayer.dataStore.tokenDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class TokenDataStore(context: Context) {
    private val tokenData = context.tokenDataStore

    suspend fun saveToken(token: String, expiration: Long){
        tokenData.edit { preferences ->
            preferences[TokenPreferences.TOKEN_KEY] = token
            preferences[TokenPreferences.token_Expired]= expiration
        }
    }

    suspend fun clearToken(){
        tokenData.edit { preferences ->
            preferences.clear()
        }
    }

    suspend fun getToken(): String? {
        return tokenData.data.map { preferences ->
            preferences[TokenPreferences.TOKEN_KEY]
        }.first()
    }

    suspend fun saveRefreshToken(refreshToken: String) {
        tokenData.edit { preferences ->
            preferences[TokenPreferences.refresh_Token_Key] = refreshToken
        }
    }

    suspend fun getRefreshToken(): String? {
        return tokenData.data.map { preferences ->
            preferences[TokenPreferences.refresh_Token_Key]
        }.first()
    }

    suspend fun isTokenExpired(): Boolean{
        val timeOfE = tokenData.data.map { preferences ->
            preferences[TokenPreferences.token_Expired]?: 0L
        }.first()
        return timeOfE<System.currentTimeMillis()
    }

    val tokenFlow: Flow<String?> = tokenData.data.map { preferences ->
        preferences[TokenPreferences.TOKEN_KEY] }
    }