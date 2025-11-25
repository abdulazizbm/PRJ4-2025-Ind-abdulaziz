package com.example.studentmarketplaceapp.ui.screen.components.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.viewModel.UserViewModel

@Composable
fun LoginForm(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current)),
    onRegisterLinkClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val token by userViewModel.token
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val loginError by userViewModel.errorMessage
    var isLoading by remember { mutableStateOf(false) }
    val user by userViewModel.userFlow.collectAsState() // Observe the user flow


    var hasNavigated by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = false
            },
            label = { Text("School Email") },
            isError = emailError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        if (emailError) Text("Invalid school email", color = MaterialTheme.colorScheme.error)

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = false
            },
            label = { Text("Password") },
            isError = passwordError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        if (passwordError) Text("Password must be at least 6 characters", color = MaterialTheme.colorScheme.error)

        Button(
            onClick = {
                passwordError = password.length < 6

                if (!emailError && !passwordError) {
                    isLoading = true
                    userViewModel.login(email,password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (token.isNotEmpty()) {
            navController.navigate("home")
        }

        if (loginError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Error: $loginError", color = androidx.compose.ui.graphics.Color.Red)
        }

        Text(
            text = "Don't have an account? Register",
            modifier = Modifier.clickable { onRegisterLinkClick() },
            color = MaterialTheme.colorScheme.primary
        )
    }
}