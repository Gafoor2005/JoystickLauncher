package dev.gafoor.joysticklauncher

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    var notifications by mutableStateOf<List<String>>(emptyList())
        private set

    fun addNotification(title: String, text: String) {
        notifications = notifications + "$title: $text\n"
    }
}