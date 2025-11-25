package com.example.studentmarketplaceapp.ui.screen

import android.graphics.Bitmap
import androidx.camera.core.Preview
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.studentmarketplaceapp.Factory.PostItemFactory
import com.example.studentmarketplaceapp.model.User
import com.example.studentmarketplaceapp.model.Category
import com.example.studentmarketplaceapp.viewModel.PostItemViewModel
import java.io.File

//@OptIn( ExperimentalPermissionsApi::class)
@Composable
fun PostItemScreen(viewModel: PostItemViewModel = viewModel(factory = PostItemFactory(LocalContext.current)), navController: NavController, user: User?) {

    val context = LocalContext.current
    val toCreate by viewModel._toCreate.collectAsState()
    val categories by viewModel.categoryFlow.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    val imageBitmap = remember { mutableStateOf<Bitmap?>(null) }
    var drop by remember{ mutableStateOf(false) }
    var categoryToSelect by remember{ mutableStateOf<Category?>(null) }

    val cameraPermissionGranted = remember { mutableStateOf(false) }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted.value = isGranted
        if (!isGranted) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    // Camera preview result
    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        imageBitmap.value = bitmap
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(12.dp)) {
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

    Column (
        modifier = Modifier
            .fillMaxSize().padding((12.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
            value = categoryToSelect?.name?: "Select category",
            onValueChange = {/*TBD?*/},
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { drop = !drop }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { drop = !drop }
        )
        DropdownMenu(
            expanded = drop,
            onDismissRequest = { drop = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        categoryToSelect = category
                        drop = false
                    }
                )
            }
        }
        }
        if (categoryToSelect == null) {
            Text("Please select a category", color = Color.Red)
        } else {
            Text("Category selected: ${categoryToSelect?.name}", color = Color.Green)
        }

        OutlinedTextField(
            value = title,
            onValueChange = {title = it},
            label = {Text("Product name")},
            modifier = Modifier.fillMaxWidth()
        )
        if (title.isEmpty()){
            Text("Please provide product name", color = Color.Red)
        }else{
            Text("valid", color = Color.Green)
        }

        val maxDesc = 150                                         //

        OutlinedTextField(
            value = description,
            onValueChange = {
                if(it.length <= maxDesc)
                    description = it
            },
            label = {Text("Describe your product")},
            modifier = Modifier.fillMaxWidth(),
            supportingText = {
                Text(
                    text = "${description.length} / $maxDesc",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        )
        if (description.isEmpty()){
            Text("Please provide product description", color = Color.Red)
        }else{
            Text("valid", color = Color.Green)
        }

        OutlinedTextField(
            value = price,
            onValueChange = {price = it},
            label = {Text("Price")},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Camera permission request
        if (!cameraPermissionGranted.value) {
            Button(onClick = { cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA) }) {
                Text("Grant Camera Permission")
            }
        } else {
            // Camera action button
            Button(onClick = { cameraLauncher.launch() }) {
                Text("Take Photo")
            }
        }

        // Display selected image
        imageBitmap.value?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = "Preview", modifier = Modifier.size(100.dp))
        }

        Button(onClick = {
            val priceVal = price.toDoubleOrNull()
            val name = title
            val p = price
            val desc = description

            if(desc.isEmpty() && p.isEmpty() && name.isEmpty()){
                Toast.makeText(context, "You are missing several information", Toast.LENGTH_SHORT).show()
                return@Button
            }


            if (name.isEmpty()){
                Toast.makeText(context, "Provide product name", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if(desc.isEmpty()){
                Toast.makeText(context, "Provide description", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if(p.isEmpty()){
                Toast.makeText(context, "Provide a valid price", Toast.LENGTH_SHORT).show()
                return@Button
            }

            if (priceVal == null) {
                Toast.makeText(context, "Invalid price", Toast.LENGTH_SHORT).show()
                return@Button
            }

            val bitmap = imageBitmap.value
            if (bitmap == null) {
                Toast.makeText(context, "Take a product photo first", Toast.LENGTH_SHORT).show()
                return@Button
            }
            val encodedImage = viewModel.encodeBitmapToBase64(bitmap)
            categoryToSelect?.let { category ->
                user?.id?.let {
                    viewModel.postProduct(title, description, priceVal, encodedImage, category.id)
                }
            } ?: run {
                Toast.makeText(context, "Please select a category before posting.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text("Post")
        }


        toCreate?.let { result ->
            result.onSuccess {
                Text("Product posted successfully!", color = Color.Green)
            }.onFailure {
                Text("Failed to post: ${it.message}", color = Color.Red)
            }
        }
    }
    }
}




