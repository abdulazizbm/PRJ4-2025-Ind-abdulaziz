package com.example.studentmarketplaceapp.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentmarketplaceapp.Factory.LogOutFactory
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.model.User
import com.example.studentmarketplaceapp.viewModel.LogoutVm
import com.example.studentmarketplaceapp.viewModel.UserViewModel

@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current)),
    navController: NavController,
    user: User?,
    logOutVm: LogoutVm = viewModel(factory = LogOutFactory(LocalContext.current)),
    ){
    val context = LocalContext.current

    var username by remember { mutableStateOf(user?.username.orEmpty()) }
    var email by remember { mutableStateOf(user?.email.orEmpty()) }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }


    var isUpdating by remember { mutableStateOf(false) }
    val logoutState by logOutVm.logoutState.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Edit Profile for ${user!!.username}")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New Password (leave blank if not changing)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text("Repeat Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                when {
                    password != password2 -> {
                        Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    }
                    username.isEmpty() -> {
                        Toast.makeText(context, "Username cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        isUpdating = true
                        user?.id?.let {
                            userViewModel.updateUser(
                                id = it,
                                username = username,
                                email = email,
                                password = password.ifEmpty { null }
                            )
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUpdating // Disable button while updating
        ) {
            Text(if (isUpdating) "Saving..." else "Save Changes")
        }

        if (isUpdating) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                logOutVm.logout()
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Logout", color = Color.Red)
        }
        LaunchedEffect(logoutState) {
            if (logoutState == true) {
                userViewModel.clearUser()
                Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
                navController.navigate("register") {
                    popUpTo(0) { inclusive = true }
                }
            } else if (logoutState == false) {
                Toast.makeText(context, "Logout failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}