package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.Message
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageService {
    @GET("messages/by-users/{user1Id}/{user2Id}/{productId}")
    suspend fun getConversation(
        @Path("user1Id") user1Id: String,
        @Path("user2Id") user2Id: String,
        @Path("productId") productId: String
    ): List<Message>

    @POST("messages")
    suspend fun sendMessage(@Body message: Any): Message

    @GET("/messages/overview/{userId}")
    suspend fun getUserChats(@Path("userId") userId: String): List<Message>


}