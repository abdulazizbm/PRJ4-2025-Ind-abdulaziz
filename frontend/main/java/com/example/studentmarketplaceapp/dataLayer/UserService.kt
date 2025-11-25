package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.Login
import com.example.studentmarketplaceapp.model.LoginToken
import com.example.studentmarketplaceapp.model.RefreshTokenRequest
import com.example.studentmarketplaceapp.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

data class UserInfo(
    val username: String,
    val schoolMail: String,
    val password: String
)

interface UserService{

    @GET("/users/{id}")
    suspend fun getUser(@Header("Authorization") token: String, @Path("_id") id : String): User

    @GET("/users/me")
    suspend fun getCurrentUser(@Header("Authorization") token: String): User

    @POST("/users")
    suspend fun createUser(@Body user: User): Response<User>

    @PATCH("users/{id}")
    suspend fun updateUser(@Path("id") id: String, @Body user: User): Response<Void>

    @POST("/users/login")
    suspend fun login (@Body userLogin: Login): LoginToken

    @POST("/users/refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): LoginToken

    @POST("/users/logout")
    suspend fun logout(
        @Header("Authorization") token: String,
        @Body refreshTokenRequest: RefreshTokenRequest
    ): Response<Void>

    @PATCH("users/{id}/fcm-token")
    suspend fun updateUserFCMToken(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body payload: Map<String, String>
    ): Response<Unit>
}