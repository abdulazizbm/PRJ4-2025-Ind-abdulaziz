package com.example.studentmarketplaceapp.ui.screen

import android.util.Base64
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.studentmarketplaceapp.model.Message
import com.example.studentmarketplaceapp.model.Product
import com.example.studentmarketplaceapp.viewModel.ChatViewModel
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.viewModel.MessageUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter



@Composable

fun ChatScreen(
    navController: NavController,
    senderId: String,
    receiverId: String,
    productId: String,
    chatViewModel: ChatViewModel

) {
    val messagesFlow: StateFlow<List<MessageUIState>> = chatViewModel.messages
    val messages by messagesFlow.collectAsState()
    Log.d("ChatScreen", "Message list updated in UI: ${messages.map { it.message.content }}")
    var messageText by remember { mutableStateOf("") }

    var product by remember { mutableStateOf<Product?>(null) }
    var productImage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImageUri = uri
    }

    LaunchedEffect(Unit) {
        Log.d("ChatScreen", "ChatScreen launched for sender=$senderId receiver=$receiverId product=$productId")
        chatViewModel.loadMessages(senderId, receiverId, productId)
        try {
            val fetchedProduct = RetrofitInstance.productService.getProducts(productId)
            val imageResponse = RetrofitInstance.productImgService.findByProductId(productId)
            product = fetchedProduct
            productImage = imageResponse?.image?.substringAfter("base64,")
        } catch (e: Exception) {}
    }


    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product?.title ?: "Chat",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product?.price?.let { "$${it}" } ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            productImage?.let {
                val bmp = decodeBase64ToBitmap(it)
                bmp?.let { bitmap ->
                    Image(
                        painter = BitmapPainter(bitmap.asImageBitmap()),
                        contentDescription = "Product Image",
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }


        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { msgState ->
                MessageBubble(
                    message = msgState.message,
                    imageBase64 = msgState.imageBase64,
                    isCurrentUser = msgState.message.sender_id == senderId
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(Icons.Default.Image, contentDescription = "Attach Image")
            }

            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    Log.d("ChatUI", "Send button clicked")
                    if (messageText.isNotBlank() || selectedImageUri != null) {
                        val base64Image = selectedImageUri?.let { uri ->
                            val inputStream = context.contentResolver.openInputStream(uri)
                            val bytes = inputStream?.readBytes()
                            Base64.encodeToString(bytes, Base64.DEFAULT)
                        }

                        Log.d("ChatUI", "Sending: '$messageText' image=${base64Image?.length ?: "none"}")
                        chatViewModel.sendMessage(senderId, receiverId, productId, messageText, base64Image)
                        messageText = ""
                        selectedImageUri = null
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, imageBase64: String?, isCurrentUser: Boolean) {
    val bubbleColor = if (isCurrentUser) Color(0xFFDCF8C6) else Color(0xFFE0E0E0)
    val boxAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start
    Log.d("MessageBubble", "Rendering message from ${message.sender_id}: '${message.content}' image=${!imageBase64.isNullOrBlank()}")

    val time = try {
        val parsedTime = ZonedDateTime.parse(message.sent_at)
        parsedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
    } catch (e: Exception) {
        ""
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = boxAlignment
    ) {
        Column(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(12.dp))
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            if (message.content.isNotBlank()) {
                Text(text = message.content, textAlign = textAlign)
                Spacer(modifier = Modifier.height(4.dp))
            }

            imageBase64?.let {
                val bitmap = decodeBase64ToBitmap(it)
                bitmap?.let { bmp ->
                    Image(
                        painter = BitmapPainter(bmp.asImageBitmap()),
                        contentDescription = "Message Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            Text(
                text = time,
                style = MaterialTheme.typography.labelSmall,
                color = Color.DarkGray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = textAlign
            )
        }
    }
}









