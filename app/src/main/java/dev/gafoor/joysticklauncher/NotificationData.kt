package dev.gafoor.joysticklauncher

import android.app.PendingIntent

data class NotificationData(
    val key: String,
    val pendingIntent: PendingIntent?,
    val appName: String,
    val title: String,
    val content: String,
    val time: String, // e.g., "2m ago"
    val clear: () -> Unit
)

