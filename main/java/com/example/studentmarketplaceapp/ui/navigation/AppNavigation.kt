package com.example.studentmarketplaceapp.ui.navigation

import ProductDetailScreen
import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.studentmarketplaceapp.Factory.LogOutFactory
import com.example.studentmarketplaceapp.Factory.PaymentViewModelFactory
import com.example.studentmarketplaceapp.Factory.PostItemFactory
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.dataLayer.TokenDataStore.TokenDataStore
import com.example.studentmarketplaceapp.model.User
import com.example.studentmarketplaceapp.ui.screen.ChatOverviewScreen
import com.example.studentmarketplaceapp.ui.screen.ChatScreen
import com.example.studentmarketplaceapp.ui.screen.EditProfileScreen
import com.example.studentmarketplaceapp.ui.screen.HomeScreen
import com.example.studentmarketplaceapp.ui.screen.LoginView
import com.example.studentmarketplaceapp.ui.screen.PaymentScreen
import com.example.studentmarketplaceapp.ui.screen.PostItemScreen
import com.example.studentmarketplaceapp.ui.screen.RegisterView
import com.example.studentmarketplaceapp.ui.screen.SearchResultScreen
import com.example.studentmarketplaceapp.ui.screen.components.bottom_nav.BottomNavigationBar
import com.example.studentmarketplaceapp.viewModel.ChatViewModel
import com.example.studentmarketplaceapp.viewModel.HomePageViewModel
import com.example.studentmarketplaceapp.viewModel.LogoutVm
import com.example.studentmarketplaceapp.viewModel.PaymentViewModel
import com.example.studentmarketplaceapp.viewModel.PostItemViewModel
import com.example.studentmarketplaceapp.viewModel.SearchViewModel
import com.example.studentmarketplaceapp.viewModel.UserViewModel

@Composable
fun AppNavigation(
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current)),
    tokenDataStore: TokenDataStore = TokenDataStore(LocalContext.current)
    ) {
    val searchViewModel: SearchViewModel = viewModel()
    val navController = rememberNavController()
    val chatViewModel: ChatViewModel = viewModel()


    val token = tokenDataStore.tokenFlow.collectAsState(initial = "").value

    LaunchedEffect(token) {
        if (token != null) {
            if (token.isNotEmpty()) {
                userViewModel.fetchUser()
            }
        }else{
            Log.d("AppNavigation","no token found")
        }
    }

    val user by userViewModel.userFlow.collectAsState(initial = null)
    LaunchedEffect(user) {
        user?.id?.let { userId ->
            //TODO: Get user listings
        }
    }

//    userID = user?.id ?: 0

    Scaffold(
         bottomBar = {
             user?.id?.let {
                 BottomNavigationBar(
                     navController = navController,
                     userID = it
                 )
             }
         }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if(user != null)"home" else "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                val homePageViewModel: HomePageViewModel = viewModel()

                HomeScreen(
                    navController = navController,
                    viewModel = homePageViewModel,
                    searchViewModel = searchViewModel
                )            }
            composable("productDetail/{productId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                if (productId != null) {
                    ProductDetailScreen(
                        navController = navController,
                        productId = productId
                    )
                }
            }

            composable("register") {
                RegisterView(navController)
            }
            composable("login") {
                LoginView(navController, userViewModel)
            }
            composable("searchResults") {
                val searchViewModel: SearchViewModel = viewModel()
                SearchResultScreen(viewModel = searchViewModel, navController = navController)
            }
            composable("postProduct") {
                PostItemScreen(navController = navController, user = user)
            }
            composable("chatOverview/{userId}") { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
                ChatOverviewScreen(navController = navController, userId = userId)
            }
            composable("chat/{senderId}/{receiverId}/{productId}") { backStackEntry ->
                val senderId = backStackEntry.arguments?.getString("senderId")
                val receiverId = backStackEntry.arguments?.getString("receiverId")
                val productId = backStackEntry.arguments?.getString("productId")

                if (senderId != null && receiverId != null && productId != null) {
                    ChatScreen(
                        navController = navController,
                        senderId = senderId,
                        receiverId = receiverId,
                        productId = productId,
                        chatViewModel = chatViewModel
                    )
                }

            }

            composable("editProfile/{userId}") { backStackEntry ->
                val userIdArg = backStackEntry.arguments?.getString("userId")
                Log.d("AppNav", "Navigated to editProfile with userId = $userIdArg")

                if (!userIdArg.isNullOrEmpty()) {
                    EditProfileScreen(
                        userViewModel = userViewModel,
                        navController = navController,
                        user = user
                    )
                } else {
                    Log.e("AppNav", "Invalid or missing userId: $userIdArg")
                }
            }
            composable("payment/{productId}/{amount}/{sellerId}") { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                val amount = backStackEntry.arguments?.getString("amount") ?: "0.00"
                val sellerId = backStackEntry.arguments?.getString("sellerId")

                Log.d("AppNavigation", "Payment route received: Product ID: $productId, Amount: $amount, Seller ID: $sellerId")

                if (productId != null && sellerId != null) {
                    val paymentViewModel: PaymentViewModel = viewModel(factory = PaymentViewModelFactory(LocalContext.current))
                    PaymentScreen(
                        viewModel = paymentViewModel,
                        productId = productId,
                        amount = amount,
                        sellerId = sellerId,
                        userViewModel = userViewModel, // Pass the userViewModel to PaymentScreen
                        onSuccess = { // Explicitly name the onSuccess parameter
                            navController.popBackStack("home", inclusive = false)
                        }
                    )
                } else {
                    // Handle cases where productId or sellerId are missing from the route
                    Log.e("AppNavigation", "Missing productId or sellerId for payment screen.")
                    // Optionally, navigate back or show an error to the user
                    navController.popBackStack()
                }
            }

        }
    }
}

