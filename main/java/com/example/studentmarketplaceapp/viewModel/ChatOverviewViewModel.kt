package com.example.studentmarketplaceapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentmarketplaceapp.dataLayer.RetrofitInstance
import com.example.studentmarketplaceapp.model.Message
import com.example.studentmarketplaceapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ConversationPreview(
    val receiverId: String,
    val productId: String,
    val lastMessage: String,
    val productTitle: String,
    val productImage: String? = null
)

class ChatOverviewViewModel : ViewModel() {
    private val _conversations = MutableStateFlow<List<ConversationPreview>>(emptyList())
    val conversations: StateFlow<List<ConversationPreview>> = _conversations

    fun loadConversations(userId: String) {
        viewModelScope.launch {
            try {
                val messages = RetrofitInstance.msgService.getUserChats(userId)

                val grouped = messages.groupBy { Pair(it.receiver_id.takeIf { id -> id != userId } ?: it.sender_id, it.product_id) }

                val previews = grouped.mapNotNull { (key, msgs) ->
                    val lastMsg = msgs.maxByOrNull { it.sent_at }
                    val product = try {
                        RetrofitInstance.productService.getProducts(key.second)
                    } catch (e: Exception) {
                        null
                    }
                    val image = try {
                        RetrofitInstance.productImgService.findByProductId(key.second)
                    } catch (e: Exception) {
                        null
                    }

                    if (lastMsg != null && product != null) {
                        ConversationPreview(
                            receiverId = key.first,
                            productId = key.second,
                            lastMessage = lastMsg.content,
                            productTitle = product.title,
                            productImage = image?.image?.substringAfter("base64,")

                        )
                    } else null
                }

                _conversations.value = previews

            } catch (e: Exception) {
                Log.e("ChatOverviewVM", "Failed to load chats", e)
            }
        }
    }
}
