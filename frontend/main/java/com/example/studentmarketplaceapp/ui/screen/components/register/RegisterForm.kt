package com.example.studentmarketplaceapp.ui.screen.components.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentmarketplaceapp.Factory.UserViewModelFactory
import com.example.studentmarketplaceapp.viewModel.UserViewModel

@Composable
fun RegisterForm(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(factory = UserViewModelFactory(LocalContext.current))
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // First Name
        OutlinedTextField(
            value = firstName,
            onValueChange = {
                firstName = it
                firstNameError = false
            },
            label = { Text("First Name") },
            isError = firstNameError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        if (firstNameError) Text("Invalid first name", color = MaterialTheme.colorScheme.error)

        // Last Name
        OutlinedTextField(
            value = lastName,
            onValueChange = {
                lastName = it
                lastNameError = false
            },
            label = { Text("Last Name") },
            isError = lastNameError,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
        if (lastNameError) Text("Invalid last name", color = MaterialTheme.colorScheme.error)

        // School Email
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
                val nameRegex = Regex("^[A-Za-z]+\$")
                val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.nl\$")

                firstNameError = !nameRegex.matches(firstName)
                lastNameError = !nameRegex.matches(lastName)
                emailError = !emailRegex.matches(email)
                passwordError = password.length < 6

                if (!firstNameError && !lastNameError && !emailError && !passwordError) {
                    userViewModel.createUser(
                        firstName + lastName,
                        email,
                        password,
                        onSuccess = { successMessage->
                            isLoading = false
                            Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
                        },
                        onError = { errorMessage->
                            isLoading = false
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                    navController.navigate("login")
                }else if(firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty()){
                    //errormessage
                }else{
                    //errormessage
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(20.dp))

        ClickableText(
            text = AnnotatedString(
                "Already have an account? Login",
                spanStyle = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ),
            onClick = {
                navController.navigate("login")
            }
        )
    }
}
