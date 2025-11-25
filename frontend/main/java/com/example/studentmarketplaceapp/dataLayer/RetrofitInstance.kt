package com.example.studentmarketplaceapp.dataLayer

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance{

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://studentmarketplace-api.blackwave-7b3ef7ff.westeurope.azurecontainerapps.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val userService: UserService by lazy{
        retrofit.create(UserService::class.java)
    }

    val catService: CategoryService by lazy{        //category
        retrofit.create(CategoryService::class.java)
    }

    val msgService: MessageService by lazy{
        retrofit.create(MessageService::class.java)
    }
    val msgImgService: MsgImgService by lazy{
        retrofit.create(MsgImgService::class.java)
    }

    val productService: ProductService by lazy{
        retrofit.create(ProductService::class.java)
    }

    val transactionService: TransactionService by lazy{
        retrofit.create(TransactionService::class.java)
    }

    val reviewService: ReviewService by lazy{
        retrofit.create(ReviewService::class.java)
    }

    val productImgService: ProductImgService by lazy{
        retrofit.create(ProductImgService:: class.java)
    }
}