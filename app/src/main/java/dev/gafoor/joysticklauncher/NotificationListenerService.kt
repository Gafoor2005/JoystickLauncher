package dev.gafoor.joysticklauncher

import android.app.Notification
import android.app.PendingIntent
import android.service.notification.StatusBarNotification
import android.service.notification.NotificationListenerService
import androidx.compose.runtime.mutableStateListOf
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.Date
import java.util.Locale

val notificationList = mutableStateListOf<NotificationData>()

class MyNotificationListenerService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        // Fetch existing notifications when the service is connected
        notificationList.clear()
        val activeNotifications = activeNotifications
        activeNotifications?.forEach { sbn ->
            addNotificationToList(sbn)
        }
    }
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        // Handle real-time notifications
        addNotificationToList(sbn)

    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        if(sbn!=null)
            removeNotificationFromList(sbn.key)
    }

    private fun addNotificationToList(sbn: StatusBarNotification) {
        val title = sbn.notification.extras.getCharSequence(Notification.EXTRA_TITLE)?.toString() ?: ""
        val content = sbn.notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: ""

        val key = sbn.key
        val packageName = sbn.packageName
        val postTime = sbn.postTime
        val pendingIntent: PendingIntent? = sbn.notification.contentIntent
        val clear = {
            removeNotificationFromList(notificationKey = sbn.key)

        }


        val formattedTime = getFormattedTime(postTime)
        if(title == ""){
            return
        }
        val notification = NotificationData(
            key = key,
            pendingIntent =  pendingIntent,
            appName = packageName,
            title = title ?: "No title",
            content = content ?: "No content",
            time = formattedTime,
            clear,
        )

        notificationList.add(notification) // Add to your list for display
    }

    private fun removeNotificationFromList (notificationKey: String) {


        // Find and remove the notification with the matching key
        val iterator = notificationList.iterator()
        while (iterator.hasNext()) {
            val notification = iterator.next()
            if (notification.key == notificationKey) {
                iterator.remove() // Remove the notification from the list
                break
            }
        }
    }

}



fun getFormattedTime(postTime: Long): String {
    val now = Instant.now()
    val notificationTime = Instant.ofEpochMilli(postTime)

    // Calculate the duration between the current time and the notification post time
    val duration = Duration.between(notificationTime, now)

    // Convert the duration to a human-readable format
    return when {
        duration.toMinutes() < 1 -> "Just now"
        duration.toMinutes() < 60 -> "${duration.toMinutes()}m ago"
        duration.toHours() < 24 -> "${duration.toHours()}h ago"
        else -> {
            val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            dateFormat.format(Date(postTime)) // Formatting into 12-hour format
        }
    }
}

