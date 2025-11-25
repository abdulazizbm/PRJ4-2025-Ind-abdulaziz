package com.example.studentmarketplaceapp.ui.screen


import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.studentmarketplaceapp.ui.screen.components.login.LoginForm
import com.example.studentmarketplaceapp.viewModel.UserViewModel

@Composable
fun LoginView(
    navController: NavController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoginForm(
            navController,
            userViewModel,
            onRegisterLinkClick = {
                navController.navigate("register")
            }
        )
    }
}