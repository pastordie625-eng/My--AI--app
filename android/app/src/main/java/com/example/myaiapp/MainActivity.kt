package com.example.myaiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.example.myaiapp.models.Message
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment

class MainActivity : ComponentActivity() {
    // Change this to your backend URL (http://10.0.2.2:8080/ for emulator local)
    private val backendUrl = "http://10.0.2.2:8080/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = ChatApi.create(backendUrl)
        val repo = ChatRepository(api)
        setContent {
            MaterialTheme {
                ChatScreen(repo)
            }
        }
    }
}

@Composable
fun ChatScreen(repo: ChatRepository) {
    var text by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<Message>() }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
            items(messages) { m ->
                val alignment = if (m.isUser) Alignment.End else Alignment.Start
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = if (m.isUser) Arrangement.End else Arrangement.Start) {
                    Card(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = m.text,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message") }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (text.isBlank()) return@Button
                val userMsg = Message(text = text, isUser = true)
                messages += userMsg
                val toSend = text
                text = ""
                scope.launch {
                    try {
                        val reply = repo.sendMessage(toSend)
                        messages += reply
                    } catch (e: Exception) {
                        messages += Message(text = "Error: ${e.message}", isUser = false)
                    }
                }
            }) {
                Text("Send")
            }
        }
    }
}