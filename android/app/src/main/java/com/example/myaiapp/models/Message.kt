package com.example.myaiapp.models

data class Message(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isUser: Boolean
)