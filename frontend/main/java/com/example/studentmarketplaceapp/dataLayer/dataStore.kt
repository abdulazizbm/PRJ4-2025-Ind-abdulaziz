package com.example.studentmarketplaceapp.dataLayer.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.tokenDataStore:DataStore<Preferences> by preferencesDataStore(name = "token-data-store")

object TokenPreferences{
    val TOKEN_KEY = stringPreferencesKey("userToken")
    val refresh_Token_Key = stringPreferencesKey("refreshUserToken")
    val token_Expired = longPreferencesKey("tokenExpired")
}