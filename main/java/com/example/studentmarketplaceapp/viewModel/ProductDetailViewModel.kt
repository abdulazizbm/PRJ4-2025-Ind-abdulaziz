package com.example.studentmarketplaceapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.model.Product
import com.example.studentmarketplaceapp.model.ProductImg
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProductDetailState(
    val product: Product? = null,
    val imageUrl: String? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ProductDetailViewModel(
    private val initialState: ProductDetailState = ProductDetailState()
) : ViewModel() {

    private val stateMutable = MutableStateFlow(initialState)
    val state: StateFlow<ProductDetailState> = stateMutable

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            try {
                stateMutable.value = ProductDetailState(isLoading = true)

                val product = RetrofitInstance.productService.getProducts(productId)
                Log.d("ProductDetail", "Fetching product with ID: $productId")

                val productImg = try {
                    RetrofitInstance.productImgService.findByProductId(productId)
                } catch (e: Exception) {
                    null
                }


                stateMutable.value = ProductDetailState(
                    product = product,
                    imageUrl = productImg?.image?.substringAfter("base64,"),
                    isLoading = false
                )
            } catch (e: Exception) {
                stateMutable.value = ProductDetailState(
                    error = "Failed to load product: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }
}

