package com.example.studentmarketplaceapp.ui.screen
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import android.media.Image
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.studentmarketplaceapp.model.Product
import com.example.studentmarketplaceapp.viewModel.HomePageViewModel
import com.example.studentmarketplaceapp.viewModel.HomeScreenState
import com.example.studentmarketplaceapp.viewModel.ProductUIState
import com.example.studentmarketplaceapp.viewModel.SearchViewModel
import java.time.LocalDate

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Home",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
@Composable
fun CategoryAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Category",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light

        )
    }
}
@Composable
fun newsfeedAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Newsfeed",
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 18.sp,
            fontWeight = FontWeight.Light

        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(viewModel: SearchViewModel, navController: NavController) {

    val toSearch by viewModel.toSearchFlow.collectAsState()
    val context = LocalContext.current

    OutlinedTextField(
        value = toSearch,
        onValueChange = {viewModel.onSearchQueryChanged(it)},
        singleLine = true,
        placeholder = { Text("Search products") },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),colors = TextFieldDefaults.outlinedTextFieldColors(
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color(0xFFCCCCCC)
        ),
    )
    Spacer(modifier = Modifier.height(8.dp))

    Button(
        onClick = {
            val searchTerm = toSearch
            if (searchTerm.isEmpty()){
                Toast.makeText(context, "nothing to search", Toast.LENGTH_SHORT).show()
                return@Button
            }
            viewModel.searchProduct()
            navController.navigate("searchResults")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text("Search")
    }
}

@Composable
fun CategoryFilters(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            AssistChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedCategory == category) Color.Black else Color.LightGray,
                    labelColor = if (selectedCategory == category) Color.White else Color.Black
                )
            )
        }
    }
}



@Composable
fun ProductGrid(products: List<ProductUIState>, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(products) { item ->
            ProductCard(product = item.product, imageUrl = item.imageUrl) {
                navController.navigate("productDetail/${item.product.id}")
            }
        }
    }
}


fun decodeBase64ToBitmap(base64String: String): Bitmap? {
    return try {
        val imageBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: Exception) {
        null
    }
}

@Composable
fun ProductCard(
    product: Product,
    imageUrl: String?,
//    sellerName: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            imageUrl?.let {
                val bitmap = decodeBase64ToBitmap(it)
                bitmap?.let { bmp ->
                    Image(
                        painter = BitmapPainter(bmp.asImageBitmap()),
                        contentDescription = "Product image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

//            Text(
//                text = "By $sellerName",
//                style = MaterialTheme.typography.bodySmall,
//                fontWeight = FontWeight.Light,
//                color = Color.Gray,
//                maxLines = 1
//            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF555555)
            )
        }
    }
}

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomePageViewModel = viewModel(),
    searchViewModel: SearchViewModel
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProduct()
    }

    Scaffold() { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {

            TopAppBar()

            Spacer(modifier = Modifier.height(8.dp))

            // Search bar
            SearchBar(navController = navController, viewModel = searchViewModel)

            Spacer(modifier = Modifier.height(16.dp))

            CategoryAppBar()

            Spacer(modifier = Modifier.height(4.dp))

            CategoryFilters(
                categories = state.categories,
                selectedCategory = state.selectedCategory,
                onCategorySelected = { category ->
                    viewModel.filterByCategory(category)
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Newsfeed title
            newsfeedAppBar()

            Spacer(modifier = Modifier.height(8.dp))

            // Product Grid or Loading/Error
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = state.error ?: "Unknown Error",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                else -> {
                    ProductGrid(products = state.products, navController = navController)
                }
            }
        }
    }
}





//@Preview(showBackground = true)
//@Composable
//fun HomeScreenPreview() {
//    val mockProducts = listOf(
//        Product(
//            productId = 1,
//            sellerId = 2,
//            title = "Dress",
//            description = "Nice summer dress",
//            price = 25.00,
//            status = true,
//            createdAt = LocalDate.now(),
//            categoryId = 1
//        ),
//        Product(
//            productId = 2,
//            sellerId = 3,
//            title = "Short",
//            description = "Comfortable shorts",
//            price = 25.00,
//            status = true,
//            createdAt = LocalDate.now(),
//            categoryId = 1
//        ),
//        Product(
//            productId = 3,
//            sellerId = 4,
//            title = "T-shirt",
//            description = "Cotton T-shirt",
//            price = 25.00,
//            status = true,
//            createdAt = LocalDate.now(),
//            categoryId = 1
//        ),
//        Product(
//            productId = 4,
//            sellerId = 5,
//            title = "Pants",
//            description = "Jeans pants",
//            price = 25.00,
//            status = true,
//            createdAt = LocalDate.now(),
//            categoryId = 1
//        )
//    )
//
//    val mockProductStates = mockProducts.map {
//        ProductUIState(product = it, imageUrl = "https://via.placeholder.com/300")
//    }
//
//    val previewState = HomeScreenState(
//        products = mockProductStates,
//        isLoading = false
//    )
//
//    val fakeViewModel = HomePageViewModel(previewState)
//
//    val navController = rememberNavController()
//
//    HomeScreen(navController = navController, viewModel = fakeViewModel)
//}


@Preview(showBackground = true)
@Composable
fun CoilImageTest() {
    Image(
        painter = rememberAsyncImagePainter("https://1861.ca/cdn/shop/files/anastriana-red-ES-1.jpg"),
        contentDescription = "Test Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentScale = ContentScale.Crop
    )
}
