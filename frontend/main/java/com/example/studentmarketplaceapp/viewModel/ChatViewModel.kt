package com.example.studentmarketplaceapp.viewModel

import android.util.Log
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.Instant
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.model.MessageImg
import kotlinx.coroutines.launch
data class MessageUIState(
    val message: Message,
    val imageBase64: String? = null
)
private data class MessageCreate(
    val content: String,
    val sent_at: String,
    val sender_id: String,
    val receiver_id: String,
    val product_id: String
)


 class ChatViewModel : ViewModel() {
     private val _messages = MutableStateFlow<List<MessageUIState>>(emptyList())
     val messages: StateFlow<List<MessageUIState>> = _messages

    fun loadMessages(user1Id: String, user2Id: String, productId: String) {
        viewModelScope.launch {
            try {
                Log.d("ChatLoad", "Loading chat between $user1Id and $user2Id for product $productId")
                val chat = RetrofitInstance.msgService.getConversation(user1Id, user2Id, productId)
                val messagesWithImages = chat.map { msg ->
                    val image = try {
                        RetrofitInstance.msgImgService.findByMessageId(msg.id)
                    } catch (e: Exception) {
                        null
                    }
                    MessageUIState(message = msg, imageBase64 = image?.image?.substringAfter("base64,"))
                }
                _messages.value = messagesWithImages
                Log.d("ChatLoad", "Loaded ${_messages.value.size} messages: ${_messages.value.map { it.message.content }}")

            } catch (e: Exception) {
                // handle error
            }
        }
    }

     fun sendMessage(senderId: String, receiverId: String, productId: String, content: String, imageBase64: String? = null) {
         viewModelScope.launch {
             try {
                 Log.d("ChatSend", "Preparing message for sending")

                 val messageToSend = MessageCreate(
                     content = content,
                     sent_at = Instant.now().toString(),
                     sender_id = senderId,
                     receiver_id = receiverId,
                     product_id = productId
                 )

                 val createdMsg = RetrofitInstance.msgService.sendMessage(messageToSend)
                 Log.d("ChatSend", "Successfully sent message: $createdMsg")

                 try {
                     if (!imageBase64.isNullOrBlank()) {
                         val msgImg = MessageImg(
                             message_id = createdMsg.id!!,
                             image = "data:image/jpeg;base64,$imageBase64"
                         )
                         val response = RetrofitInstance.msgImgService.sendMessageImage(msgImg)
                         Log.d("ChatSend", "Image sent successfully: $response")
                     }
                 } catch (e: Exception) {
                     Log.e("ChatSend", "Failed to send image", e)
                 }


                 _messages.value = _messages.value + MessageUIState(createdMsg, imageBase64)

             } catch (e: Exception) {
                 Log.e("ChatSend", "Error sending message", e)
             }
         }
     }


 }





