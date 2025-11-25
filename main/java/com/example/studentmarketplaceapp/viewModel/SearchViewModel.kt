package com.example.studentmarketplaceapp.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchProductUIState(
    val product: Product,
    val imageUrl: String?
)

class SearchViewModel: ViewModel(){

    private val service = RetrofitInstance.productService
    private val productImgService = RetrofitInstance.productImgService


    private val toSearch = MutableStateFlow("")
    val toSearchFlow: StateFlow<String> = toSearch

    private val productList = MutableStateFlow<List<SearchProductUIState>>(emptyList())
    val productListFlow: StateFlow<List<SearchProductUIState>> = productList


    fun onSearchQueryChanged(newSearch: String) {
        toSearch.value = newSearch
    }

    fun searchProduct() {
        viewModelScope.launch {
            try {
                val result = service.getSearchedProducts(toSearch.value)
                val searchProductUIState = result.map { product ->
                    val imageUrl = try{
                        productImgService.findByProductId(product.id)
                    }catch (e:Exception){
                        Log.d("SearchViewModel","no img found")
                        null;
                    }
                    val img = imageUrl?.image
                    SearchProductUIState(product = product, imageUrl = img)
                }
                Log.d("SearchViewModel", "Search results count: ${searchProductUIState.size}")
                searchProductUIState.forEachIndexed { index, state ->
                    Log.d("SearchViewModel", "Result [$index]: Title=${state.product.title}, ImageUrl=${state.imageUrl}")
                }
                productList.value = searchProductUIState
//                _navigateToResults.value = true
            } catch (e: Exception) {
                Log.d("SearchViewModel", "Error during search: ${e.message}")
                productList.value = emptyList()
            }
        }
    }
}