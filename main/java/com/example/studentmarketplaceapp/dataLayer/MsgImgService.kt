package com.example.studentmarketplaceapp.dataLayer

import com.example.studentmarketplaceapp.model.MessageImg
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MsgImgService {

    @POST("message-imgs")
    suspend fun sendMessageImage(@Body msgImg: MessageImg): MessageImg
    @GET("message-imgs/by-message/{messageId}")
    suspend fun findByMessageId(@Path("messageId") messageId: String): MessageImg?

}