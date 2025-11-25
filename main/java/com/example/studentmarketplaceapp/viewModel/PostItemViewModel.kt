package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.ProductInfo
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentmarketplaceapp.dataLayer.ProductImgInfo
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.Category


class PostItemViewModel(private val context: Context): ViewModel() {

    private val service = RetrofitInstance.productService
    private val imgService = RetrofitInstance.productImgService
    private val categoryService = RetrofitInstance.catService
    private val tokenDataStore = TokenDataStore(context)
    private val tokenSecurity = TokenSecurity(context)



    private val toCreate = MutableStateFlow<Result<Product>?>(null)
    val _toCreate: StateFlow<Result<Product>?> = toCreate

    private val categories = MutableStateFlow<List<Category>>(emptyList())
    val categoryFlow: StateFlow<List<Category>> = categories

    init {
        getCategories()
    }

    fun encodeBitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getCategories(){
        viewModelScope.launch {
            try{
                val catGotten = categoryService.getAllCategories()
                categories.value = catGotten
            }catch (e: Exception){
                Log.d("PostItemViewModel", "Error while retrieving cat: ${e.message}")
            }
        }
    }

    fun postProduct(title: String, description: String, price: Double, imageBase64: String, categoryId: String){
        viewModelScope.launch {
            try {
                val authToken = tokenSecurity.checkTokenAndRefresh()

                Log.d("AUTH_DEBUG", "JWT Token: ${authToken?.take(10)}...")

                if (authToken!=null){

                    Log.d("API_REQUEST", """
                    Sending product creation request with:
                    - Title: $title
                    - Price: $price
                    - Category: $categoryId
                    - Token: Bearer ${authToken.take(10)}...
                """.trimIndent())

                    checkUserAuth()

                    val toPost = service.createProduct("Bearer $authToken",
                        ProductInfo(
                            title = title,
                            price = price,
                            description = description,
                            categoryId = categoryId,
                        )
                    )
                    if (toPost.isSuccessful){
                        val createdProduct = toPost.body()!!

                        val img = imgService.createProductImg(
                            ProductImgInfo(
                                product_id = createdProduct.id,
                                image = imageBase64
                            )
                        )
                        if (!img.isSuccessful) {
                            val errorBody = img.errorBody()?.string()
                            Log.e("PostItemViewModel", "Image upload failed: ${img.code()} - $errorBody")
                            toCreate.value = Result.failure(Exception("Failed to upload image: ${img.code()} - $errorBody"))
                            return@launch
                        }

                        toCreate.value = Result.success(createdProduct)

                    }else{
                        val errorBody = toPost.errorBody()?.string()
                        Log.e("PostItemViewModel", "Product creation failed: ${toPost.code()} - $errorBody")
                        toCreate.value = Result.failure(Exception("Failed ${toPost.code()} - $errorBody"))
                        return@launch
                    }
                }
            }catch (e:Exception){
                Log.d("PostItemViewModel", "Error while posting: ${e.message}")
            }
        }
    }

    private fun checkUserAuth() {
        viewModelScope.launch {
            val authToken = tokenSecurity.checkTokenAndRefresh()
            if (authToken != null) {
                val response = service.debugUser("Bearer $authToken")
                if (response.isSuccessful) {
                    Log.d("USER_DEBUG", "User ID from token: ${response.body()?.get("userId")}")
                } else {
                    Log.e("USER_DEBUG", "Failed to fetch user: ${response.errorBody()?.string()}")
                }
            }
        }
    }
}