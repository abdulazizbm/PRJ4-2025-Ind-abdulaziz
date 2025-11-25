package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import android.app.Application
import androidx.lifecycle.AndroidViewModel

data class HomeScreenState(
    val products: List<ProductUIState> = emptyList(),
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "All",
    val isLoading: Boolean = true,
    val error: String? = null
)

data class ProductUIState(
    val product: Product,
    val imageUrl: String?,
    val sellerName: String
)

class HomePageViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val tokenDataStore = TokenDataStore(context)
    val token = mutableStateOf("")
    private val stateMutable = MutableStateFlow(HomeScreenState())
    val state: StateFlow<HomeScreenState> = stateMutable

    private var allProducts: List<ProductUIState> = emptyList()
    private lateinit var categoryNameToId: Map<String, String>

    fun loadProduct() {
        viewModelScope.launch {
            try {
                stateMutable.value = HomeScreenState(isLoading = true)

                val categoriesFromApi = RetrofitInstance.catService.getAllCategories()
                categoryNameToId = categoriesFromApi.associate { it.name.lowercase().trim() to it.id }
                val categoryNames = listOf("All") + categoriesFromApi.map { it.name }

                val productList = RetrofitInstance.productService.getAllProducts()
                allProducts = productList.map { product ->
                    val image = try {
                        RetrofitInstance.productImgService.findByProductId(product.id)

                    } catch (e: Exception) {
                        null
                    }

                    val seller = try {
                        RetrofitInstance.userService.getCurrentUser("Bearer $token")
                    } catch (e: Exception) {
                        null
                    }

                    ProductUIState(
                        product = product,
                        imageUrl = image?.image?.substringAfter("base64,"),
                        sellerName = seller?.username ?: "Unknown Seller"
                    )
                }

                stateMutable.value = stateMutable.value.copy(
                    categories = categoryNames,
                    products = allProducts,
                    isLoading = false
                )
            } catch (e: Exception) {
                stateMutable.value = HomeScreenState(
                    error = "Failed to load products: ${e.localizedMessage}",
                    isLoading = false
                )
            }
        }
    }

    fun filterByCategory(category: String) {
        val filtered = if (category == "All") {
            allProducts
        } else {
            val cleanedCategory = category.lowercase().trim()
            val categoryId = categoryNameToId[cleanedCategory]
            Log.d("FilterDebug", "Category ID for '$category': $categoryId")

            if (categoryId == null) {
                Log.e("FilterError", "No ID found for category: $category")
                return
            }

            val filteredList = allProducts.filter { it.product.categoryId.equals(categoryId) }
            Log.d("FilterDebug", "Filtered products count: ${filteredList.size}")
            filteredList
        }
        stateMutable.value = stateMutable.value.copy(
            selectedCategory = category,
            products = filtered
        )
    }
}



