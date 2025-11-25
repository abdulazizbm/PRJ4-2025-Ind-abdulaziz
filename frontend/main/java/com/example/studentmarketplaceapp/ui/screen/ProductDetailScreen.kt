import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.model.Product
import com.example.studentmarketplaceapp.model.User
import com.example.studentmarketplaceapp.ui.screen.decodeBase64ToBitmap
import com.example.studentmarketplaceapp.ui.viewmodel.ProductDetailState
import com.example.studentmarketplaceapp.ui.viewmodel.ProductDetailViewModel
import com.example.studentmarketplaceapp.viewModel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate

@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String,
    viewModel: ProductDetailViewModel = viewModel(),
    viewModel2: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current))
) {
    val state by viewModel.state.collectAsState()
    val user by viewModel2.userFlow.collectAsState()

    val buyerId = user?.id

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }
    LaunchedEffect(state.product) {
        state.product?.let {
            Log.d("ChatDebug", "Loaded product '${it.title}', sellerId = ${it.sellerId}")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Back Button
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (state.error != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = state.error ?: "Unknown Error", color = MaterialTheme.colorScheme.error)
        }
    } else {
        state.product?.let { product ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

                state.imageUrl?.let {
                    val bitmap = decodeBase64ToBitmap(it)
                    bitmap?.let { bmp ->
                        Image(
                            painter = BitmapPainter(bmp.asImageBitmap()),
                            contentDescription = "Product Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = product.title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = "$${product.price }", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = product.description, style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.weight(1f))
                val context = LocalContext.current

                if (product != null && user?.id != null) {
                    Button(
                        onClick = {
                            if (product.sellerId.equals(user?.id)) {
                                Toast.makeText(context, "You can't send a message to yourself", Toast.LENGTH_SHORT).show()
                            } else {
                                navController.navigate("chat/${user!!.id}/${product.sellerId}/${product.id}")
                            }

                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Chat with the seller")
                    }
                } else {
                    // You can show a disabled button or nothing
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false
                    ) {
                        Text("Loading...")
                    }
                }

                Button(
                    onClick = {
                        Log.d("ProductDetailScreen", "Navigating to payment. Product ID: ${product.id}, Price: ${product.price}, Seller ID: ${product.sellerId}")
                        navController.navigate("payment/${product.id}/${product.price}/${product.sellerId}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buy Now")
                }
            }
        }
    }
}
}



//@Preview(showBackground = true)
//@Composable
//fun ProductDetailScreenPreview() {
//    val mockProduct = Product(
//        product_id = 2,
//        sellerId = 10,
//        title = "Sample Product",
//        description = "this is the description",
//        price = 24.7,
//        status = true,
//        createdAt = LocalDate.now(),
//        category_id = 3
//    )
//
//    val mockState = ProductDetailState(
//        product = mockProduct,
//        imageUrl = "https://via.placeholder.com/300",
//        isLoading = false
//    )
//
//    val previewViewModel = ProductDetailViewModel(mockState)
//
//    val navController = rememberNavController()
//
//    ProductDetailScreen(productId = 2, navController = navController, viewModel = previewViewModel)


