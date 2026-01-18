package com.example.myaiapp

import com.example.myaiapp.models.Message

class ChatRepository(private val api: ChatApi) {
    suspend fun sendMessage(text: String): Message {
        val response = api.chat(ChatRequest(message = text))
        return Message(text = response.reply, isUser = false)
    }
}