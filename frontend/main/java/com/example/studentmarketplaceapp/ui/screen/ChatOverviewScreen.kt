package com.example.studentmarketplaceapp.ui.screen

import android.util.Base64
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentmarketplaceapp.viewModel.ChatOverviewViewModel
import com.example.studentmarketplaceapp.viewModel.ConversationPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatOverviewScreen(
    navController: NavController,
    userId: String,
    viewModel: ChatOverviewViewModel = viewModel()
) {
    val conversations by viewModel.conversations.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadConversations(userId)
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = { Text("Chats") }
        )
    }) { padding ->
        if (conversations.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No conversations yet.")
            }
        } else {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(padding)) {
                conversations.forEach { preview ->
                    ConversationCard(preview) {
                        navController.navigate("chat/$userId/${preview.receiverId}/${preview.productId}")
                    }
                    Divider()
                }
            }
        }
    }
}

@Composable
fun ConversationCard(
    preview: ConversationPreview,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        preview.productImage?.let { base64 ->
            val imageBytes = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }

        Column {
            Text(text = preview.productTitle, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = preview.lastMessage, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
