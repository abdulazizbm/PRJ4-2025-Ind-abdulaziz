package com.example.studentmarketplaceapp.ui.screen.components.bottom_nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController:NavController, userID:String) {

    Surface(
        tonalElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp), // slightly taller for central button
        color = Color.White
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                navController.navigate("home")
            }) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            }

            IconButton(onClick = {
                navController.navigate("chat/{senderId}/{receiverId}/{productId}")
            }) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
            }

            // Central "+" Button with Circle
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F3F5)),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    navController.navigate("postProduct")
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.DarkGray
                    )
                }
            }

            IconButton(onClick = {
                navController.navigate("chatOverview/$userID")
            }) {
                Icon(imageVector = Icons.Default.ChatBubble, contentDescription = "Chat")
            }

            IconButton(onClick = {
                navController.navigate("editProfile/$userID")
            }) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Profile")
            }
        }
    }
}