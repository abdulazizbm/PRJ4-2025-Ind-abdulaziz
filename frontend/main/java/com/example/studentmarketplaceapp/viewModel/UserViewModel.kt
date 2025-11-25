package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.dataLayer.UserService
import com.example.studentmarketplaceapp.model.Login
import com.example.studentmarketplaceapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.mindrot.jbcrypt.BCrypt
import retrofit2.HttpException
import java.io.IOException

class UserViewModel(private val context: Context) : ViewModel() {
    private val apiService: UserService = RetrofitInstance.userService
    private val tokenDataStore = TokenDataStore(context)

    val token = mutableStateOf("")
    val user = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = user
    var userId = mutableStateOf<String?>(null)
    var successMessage = mutableStateOf("")
    val errorMessage = mutableStateOf("")

    companion object {
        const val TOKEN_EXPIRATION_DURATION = 2 * 60 * 1000
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val expirationOfT = System.currentTimeMillis() + TOKEN_EXPIRATION_DURATION
                val response = apiService.login(Login(email, password))
                token.value = response.token

                tokenDataStore.saveToken(response.token, expirationOfT)
                tokenDataStore.saveRefreshToken(response.refreshToken)

                Log.d("UserViewModel", "Token saved with expiration: $expirationOfT")

                fetchUser()

            } catch (e: Exception) {
                errorMessage.value = "Error during login: ${e.message}"
                Log.d("LoginViewModel", "Login failed: ${e.message}")
            }
        }
    }


        fun fetchUser() {
        viewModelScope.launch {
            try {
                var authToken = tokenDataStore.getToken()

                if (authToken.isNullOrEmpty()) {
                    errorMessage.value = "Token missing; please log in again."
                    return@launch
                }

                Log.d("UserViewModel", "Using token: $authToken")

                try {
                    val fetchedUser = apiService.getCurrentUser("Bearer $authToken")
                    user.value = fetchedUser
                    return@launch
                } catch (e: HttpException) {
                    if (e.code() == 401) {
                        Log.d("UserViewModel", "Token invalid. Attempting refresh...")
                        val refreshedToken = TokenSecurity(context).checkTokenAndRefresh()
                        if (!refreshedToken.isNullOrEmpty()) {
                            token.value = refreshedToken
                            val retriedUser = apiService.getCurrentUser("Bearer $refreshedToken")
                            user.value = retriedUser
                            return@launch
                        }
                    }
                    throw e
                }
            } catch (e: Exception) {
                Log.d("UserViewModel", "Error fetching user: ${e.message}")
                user.value = null
                errorMessage.value = "Failed to fetch user data: ${e.message}"
            }
        }
    }

    fun createUser(
        username: String,
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch{
            try {
                val newUser = User(
                    null,
                    username,
                    email,
                    password,
                )
                val result = apiService.createUser(
                    newUser
                )
                if (result.isSuccessful) {
                    onSuccess("Success creating user")
                    user.value = result.body()
                }else{
                    onError(result.errorBody().toString())
                }
            } catch (e: IOException) {
                onError("Network failure")
            }catch (e: HttpException){
                onError("Request failed")
            }
        }
    }

    fun updateUser(
        id: String,
        username: String,
        email: String,
        password: String? = null
    ) {
        viewModelScope.launch {
            try {
                val currentUser = user.value ?: return@launch

                // Use existing password if none is provided
                val finalPassword = if (password.isNullOrEmpty()) currentUser.password else hashPassword(password)

                // Create the updated user object
                val updatedUser = currentUser.copy(
                    username = username,
                    email = email,
                    password = finalPassword
                )

                // Call the API
                val response = apiService.updateUser(id, updatedUser)
                if (response.isSuccessful) {
                    Log.d("EditProfile", "User updated successfully: ${response.body()}")
                    user.value = updatedUser
                    successMessage.value = "Profile updated successfully."
                    println("Profile updated successfully.")
                } else {
                    errorMessage.value = "Failed to update profile: ${response.code()}"
                    Log.e("EditProfile", "Failed to update: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage.value = "Failed to update profile: ${e.message}"
                println("Error updating profile: ${e.stackTraceToString()}")
            }
        }
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun clearUser(){
        user.value = null
    }
}
